package com.example.alcohol_free_day.domain.user.repository.impl;

import com.example.alcohol_free_day.domain.history.entity.QHistory;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.QUser;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.repository.UserRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // TODO 쿼리문 짜기
    @Override
    public UserResponse.HomeUserInfo findHomeInfo(User user) {
        QHistory history = QHistory.history;

        // 오늘 날짜와 시작일 계산
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1); // 이번 달의 첫 번째 날

        // LocalDate를 Date로 변환
        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        final float sojuPrice = 1600;
        final float winePrice = 20000;
        final float beerPrice = 2500;
        final float makgeolliPrice = 1500;

        // 이번 달 음주량 계산
        Float monthlyConsumption = queryFactory
                .select(history.sojuConsumption.sum()
                        .add(history.wineConsumption.sum())
                        .add(history.beerConsumption.sum())
                        .add(history.makgeolliConsumption.sum()))
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startDate, endDate))) // 시작일과 종료일 설정
                .fetchOne();

        // 예상 지출액 계산
        Long expectedCost = (long) Math.round(monthlyConsumption * (sojuPrice + winePrice + beerPrice + makgeolliPrice));

        // 연속 음주 없는 날 계산 (예시)
//        Long alcoholFreeDays = queryFactory
//                .select(/* TODO 연속 음주 없는 날 수 계산 로직 */)
//                .from(history)
//                .where(history.user.eq(user))
//                .fetchOne();

        // 보유 포인트 (여기서는 User 객체에서 가져오는 것으로 가정)
        Long point = user.getPoint(); // User 객체에 포인트 필드가 있다고 가정

        return UserResponse.HomeUserInfo.builder()
                .monthlyConsumption(monthlyConsumption)
                .expectedCost(expectedCost)
//                .alcoholFreeDays(alcoholFreeDays)
                .point(point)
                .build();
    }

    @Override
    public UserResponse.HistoryUserInfo findHistoryInfo(User user) {
        return null;
    }

    @Override
    public UserResponse.WeeklyStatistics findWeeklyStatistics(User user) {
        return null;
    }

    @Override
    public UserResponse.MonthlyStatistics findMonthlyStatistics(User user) {
        return null;
    }
}
