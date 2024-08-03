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
    final float sojuPrice = 5000;
    final float winePrice = 20000;
    final float beerPrice = 5000;
    final float makgeolliPrice = 4000;
    final float sojuKcal = 332;
    final float wineKcal = 625;
    final float beerKcal = 500;
    final float makgeolliKcal = 345;

    @Override
    public UserResponse.HomeUserInfo findHomeInfo(User user) {
        QHistory history = QHistory.history;

        // 현재 날짜와 이번 달의 첫 번째 및 마지막 날 계산
        LocalDate now = LocalDate.now();
        LocalDate thisMonthStart = now.withDayOfMonth(1);
        LocalDate thisMonthEnd = now.withDayOfMonth(now.lengthOfMonth());

        // LocalDate를 Date로 변환
        Date start = Date.from(thisMonthStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(thisMonthEnd.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 이번 달 음주량 계산
        Float monthlyConsumption = queryFactory
                .select(history.sojuConsumption.sum()
                        .add(history.wineConsumption.sum())
                        .add(history.beerConsumption.sum())
                        .add(history.makgeolliConsumption.sum()))
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end))) // 시작일과 종료일 설정
                .fetchOne();

        // 이번 달 섭취 칼로리 계산
        Float monthlyKcal = queryFactory
                .select(history.sojuConsumption.sum().multiply(sojuKcal)
                        .add(history.wineConsumption.sum().multiply(wineKcal))
                        .add(history.beerConsumption.sum().multiply(beerKcal))
                        .add(history.makgeolliConsumption.sum().multiply(makgeolliKcal)))
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        // 예상 지출액 계산
        Float expectedCost = queryFactory
                .select(history.sojuConsumption.sum().multiply(sojuPrice)
                        .add(history.wineConsumption.sum().multiply(winePrice))
                        .add(history.beerConsumption.sum().multiply(beerPrice))
                        .add(history.makgeolliConsumption.sum().multiply(makgeolliPrice)))
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        // 보유 포인트 (여기서는 User 객체에서 가져오는 것으로 가정)
        Long point = user.getPoint(); // User 객체에 포인트 필드가 있다고 가정

        return UserResponse.HomeUserInfo.builder()
                .monthlyConsumption(monthlyConsumption)
                .monthlyCalorie(monthlyKcal)
                .expectedCost(expectedCost)
                .point(point)
                .build();
    }
}
