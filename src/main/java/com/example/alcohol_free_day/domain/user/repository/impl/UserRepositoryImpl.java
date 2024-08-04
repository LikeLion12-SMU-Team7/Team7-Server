package com.example.alcohol_free_day.domain.user.repository.impl;

import com.example.alcohol_free_day.domain.history.entity.QHistory;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.repository.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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
    public UserResponse.HomeUserInfoDto findHomeInfo(User user) {
        QHistory history = QHistory.history;

        // 현재 날짜와 이번 달의 첫 번째 및 마지막 날 계산
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        // 이번 달 음주량 계산
        Float monthlyConsumption = queryFactory
                .select(history.sojuConsumption.sum()
                        .add(history.wineConsumption.sum())
                        .add(history.beerConsumption.sum())
                        .add(history.makgeolliConsumption.sum()))
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
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

        return UserResponse.HomeUserInfoDto.builder()
                .monthlyConsumption(monthlyConsumption)
                .monthlyCalorie(monthlyKcal)
                .expectedCost(expectedCost)
                .point(user.getPoint())
                .build();
    }
}
