package com.example.alcohol_free_day.domain.history.repository.impl;

import com.example.alcohol_free_day.domain.history.entity.QHistory;
import com.example.alcohol_free_day.domain.history.repository.HistoryRepositoryCustom;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HistoryRepositoryCustomImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 주종 별 알코올 함유량 (g 단위)
    final float SOJU_ALCOHOL = 60.84f; // 소주 360ml, 알코올 도수 16.9%
    final float WINE_ALCOHOL = 101.25f; // 와인 750ml, 알코올 도수 13.5%
    final float BEER_ALCOHOL = 22.5f; // 맥주 500ml, 알코올 도수 4.5%
    final float MAKGEOLLI_ALCOHOL = 45f; // 막걸리 750ml, 알코올 도수 6%

    @Override
    public UserResponse.WeeklyStatisticsComparedDto findWeeklyCompared(User user) {
        QHistory history = QHistory.history;

        // 이번 주 (일요일부터 토요일까지)
        LocalDate now = LocalDate.now();
        LocalDate thisWeekStart = now.with(DayOfWeek.SUNDAY);
        LocalDate thisWeekEnd = now.with(DayOfWeek.SATURDAY);

        // 이번 주 (일요일부터 토요일까지)
        if (now.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            thisWeekStart = now;
            thisWeekEnd = now.plusDays(6).plusDays(1);
        } else {
            thisWeekStart = now.with(java.time.DayOfWeek.SUNDAY).minusDays(7);
            thisWeekEnd = thisWeekStart.plusDays(6).plusDays(1);
        }

        // 지난 주 (일요일부터 토요일까지)
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate lastWeekEnd = thisWeekEnd.minusWeeks(1).plusDays(1);

        // 이번 주 음주량 계산
        Float thisWeekSoju = queryFactory
                .select(history.sojuConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd)))
                .fetchOne();

        Float thisWeekWine = queryFactory
                .select(history.wineConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd)))
                .fetchOne();

        Float thisWeekBeer = queryFactory
                .select(history.beerConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd)))
                .fetchOne();

        Float thisWeekMakgeolli = queryFactory
                .select(history.makgeolliConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd)))
                .fetchOne();

        // 지난 주 음주량 계산
        Float lastWeekSoju = queryFactory
                .select(history.sojuConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd)))
                .fetchOne();

        Float lastWeekWine = queryFactory
                .select(history.wineConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd)))
                .fetchOne();

        Float lastWeekBeer = queryFactory
                .select(history.beerConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd)))
                .fetchOne();

        Float lastWeekMakgeolli = queryFactory
                .select(history.makgeolliConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd)))
                .fetchOne();

        // Null 값 처리를 위해 0으로 초기화
        thisWeekSoju = thisWeekSoju == null ? 0 : thisWeekSoju;
        thisWeekWine = thisWeekWine == null ? 0 : thisWeekWine;
        thisWeekBeer = thisWeekBeer == null ? 0 : thisWeekBeer;
        thisWeekMakgeolli = thisWeekMakgeolli == null ? 0 : thisWeekMakgeolli;

        lastWeekSoju = lastWeekSoju == null ? 0 : lastWeekSoju;
        lastWeekWine = lastWeekWine == null ? 0 : lastWeekWine;
        lastWeekBeer = lastWeekBeer == null ? 0 : lastWeekBeer;
        lastWeekMakgeolli = lastWeekMakgeolli == null ? 0 : lastWeekMakgeolli;

        // 차이 계산
        float sojuDifference = thisWeekSoju - lastWeekSoju;
        float wineDifference = thisWeekWine - lastWeekWine;
        float beerDifference = thisWeekBeer - lastWeekBeer;
        float makgeolliDifference = thisWeekMakgeolli - lastWeekMakgeolli;

        return UserResponse.WeeklyStatisticsComparedDto.builder()
                .weeklySojuCount(thisWeekSoju)
                .weeklyWineCount(thisWeekWine)
                .weeklyBeerCount(thisWeekBeer)
                .weeklyMakgeolliCount(thisWeekMakgeolli)
                .sojuDifference(sojuDifference)
                .wineDifference(wineDifference)
                .beerDifference(beerDifference)
                .makgeolliDifference(makgeolliDifference)
                .build();
    }

    @Override
    public UserResponse.MonthlyStatisticsComparedDto findMonthlyCompared(User user) {
        QHistory history = QHistory.history;

        // 이번 달 (1일부터 말일까지)
        LocalDate now = LocalDate.now();
        LocalDate thisMonthStart = now.withDayOfMonth(1);
        LocalDate thisMonthEnd = now.withDayOfMonth(now.lengthOfMonth());

        // 지난 달 (1일부터 말일까지)
        LocalDate lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDate lastMonthEnd = thisMonthEnd.minusMonths(1);

        // 이번 달 음주량 계산
        Float thisMonthSoju = queryFactory
                .select(history.sojuConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd)))
                .fetchOne();

        Float thisMonthWine = queryFactory
                .select(history.wineConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd)))
                .fetchOne();

        Float thisMonthBeer = queryFactory
                .select(history.beerConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd)))
                .fetchOne();

        Float thisMonthMakgeolli = queryFactory
                .select(history.makgeolliConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd)))
                .fetchOne();

        // 지난 달 음주량 계산
        Float lastMonthSoju = queryFactory
                .select(history.sojuConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd)))
                .fetchOne();

        Float lastMonthWine = queryFactory
                .select(history.wineConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd)))
                .fetchOne();

        Float lastMonthBeer = queryFactory
                .select(history.beerConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd)))
                .fetchOne();

        Float lastMonthMakgeolli = queryFactory
                .select(history.makgeolliConsumption.sum())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd)))
                .fetchOne();

        // Null 값 처리를 위해 0으로 초기화
        thisMonthSoju = thisMonthSoju == null ? 0f : thisMonthSoju;
        thisMonthWine = thisMonthWine == null ? 0f : thisMonthWine;
        thisMonthBeer = thisMonthBeer == null ? 0f : thisMonthBeer;
        thisMonthMakgeolli = thisMonthMakgeolli == null ? 0f : thisMonthMakgeolli;

        lastMonthSoju = lastMonthSoju == null ? 0f : lastMonthSoju;
        lastMonthWine = lastMonthWine == null ? 0f : lastMonthWine;
        lastMonthBeer = lastMonthBeer == null ? 0f : lastMonthBeer;
        lastMonthMakgeolli = lastMonthMakgeolli == null ? 0f : lastMonthMakgeolli;

        // 차이 계산
        float sojuDifference = thisMonthSoju - lastMonthSoju;
        float wineDifference = thisMonthWine - lastMonthWine;
        float beerDifference = thisMonthBeer - lastMonthBeer;
        float makgeolliDifference = thisMonthMakgeolli - lastMonthMakgeolli;

        return UserResponse.MonthlyStatisticsComparedDto.builder()
                .monthlySojuCount(thisMonthSoju)
                .monthlyWineCount(thisMonthWine)
                .monthlyBeerCount(thisMonthBeer)
                .monthlyMakgeolliCount(thisMonthMakgeolli)
                .sojuDifference(sojuDifference)
                .wineDifference(wineDifference)
                .beerDifference(beerDifference)
                .makgeolliDifference(makgeolliDifference)
                .build();
    }

    @Override
    public UserResponse.WeeklyStatisticsCountsDto findWeeklyCount(User user) {
        QHistory history = QHistory.history;

        // 이번 주 (일요일부터 토요일까지)
        LocalDate now = LocalDate.now();
        LocalDate thisWeekStart = now.with(DayOfWeek.SUNDAY);
        LocalDate thisWeekEnd = now.with(DayOfWeek.SATURDAY);

        // 이번 주 (일요일부터 토요일까지)
        if (now.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            thisWeekStart = now;
            thisWeekEnd = now.plusDays(6);
        } else {
            thisWeekStart = now.with(java.time.DayOfWeek.SUNDAY).minusDays(7);
            thisWeekEnd = thisWeekStart.plusDays(6);
        }

        // 지난 주 (일요일부터 토요일까지)
        LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
        LocalDate lastWeekEnd = thisWeekEnd.minusWeeks(1).plusDays(1);

        // 음주한 날의 횟수 계산
        Long thisWeekDrinkCount = queryFactory
                .select(history.count())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        Long lastWeekDrinkCount = queryFactory
                .select(history.date.countDistinct())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        Float thisWeekTotalAlcohol = queryFactory
                .select(
                        history.sojuConsumption.sum().multiply(SOJU_ALCOHOL)
                                .add(history.wineConsumption.sum().multiply(WINE_ALCOHOL))
                                .add(history.beerConsumption.sum().multiply(BEER_ALCOHOL))
                                .add(history.makgeolliConsumption.sum().multiply(MAKGEOLLI_ALCOHOL))
                )
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisWeekStart, thisWeekEnd)))
                .fetchOne();

        Float lastWeekTotalAlcohol = queryFactory
                .select(
                        history.sojuConsumption.sum().multiply(SOJU_ALCOHOL)
                                .add(history.wineConsumption.sum().multiply(WINE_ALCOHOL))
                                .add(history.beerConsumption.sum().multiply(BEER_ALCOHOL))
                                .add(history.makgeolliConsumption.sum().multiply(MAKGEOLLI_ALCOHOL))
                )
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastWeekStart, lastWeekEnd)))
                .fetchOne();

        // Null 값 처리를 위해 0으로 초기화
        thisWeekDrinkCount = thisWeekDrinkCount == null ? 0L : thisWeekDrinkCount;
        lastWeekDrinkCount = lastWeekDrinkCount == null ? 0L : lastWeekDrinkCount;
        thisWeekTotalAlcohol = thisWeekTotalAlcohol == null ? 0f : thisWeekTotalAlcohol;
        lastWeekTotalAlcohol = lastWeekTotalAlcohol == null ? 0f : lastWeekTotalAlcohol;

        // 차이 계산
        Long drinkCountDifference = thisWeekDrinkCount - lastWeekDrinkCount;
        Float totalAlcoholDifference = thisWeekTotalAlcohol - lastWeekTotalAlcohol;

        return UserResponse.WeeklyStatisticsCountsDto.builder()
                .drinkCount(thisWeekDrinkCount)
                .drinkCountDiff(drinkCountDifference)
                .totalAlcohol(thisWeekTotalAlcohol)
                .totalAlcoholDiff(totalAlcoholDifference)
                .build();
    }

    @Override
    public UserResponse.MonthlyStatisticsCountsDto findMonthlyCount(User user) {
        QHistory history = QHistory.history;

        // 현재 날짜와 이번 달의 첫 번째 및 마지막 날 계산
        LocalDate now = LocalDate.now();
        LocalDate thisMonthStart = now.withDayOfMonth(1);
        LocalDate thisMonthEnd = now.withDayOfMonth(now.lengthOfMonth());

        // 지난 달의 첫 번째와 마지막 날 계산
        LocalDate lastMonthStart = thisMonthStart.minusMonths(1);
        LocalDate lastMonthEnd = thisMonthEnd.minusMonths(1);

        // 이번 달 음주한 날의 횟수 계산
        Long thisMonthDrinkCount = queryFactory
                .select(history.date.countDistinct())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        // 지난 달 음주한 날의 횟수 계산
        Long lastMonthDrinkCount = queryFactory
                .select(history.date.countDistinct())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        // 이번 달 총 알코올 섭취량 계산
        Float thisMonthTotalAlcohol = queryFactory
                .select(
                        history.sojuConsumption.sum().multiply(SOJU_ALCOHOL)
                                .add(history.wineConsumption.sum().multiply(WINE_ALCOHOL))
                                .add(history.beerConsumption.sum().multiply(BEER_ALCOHOL))
                                .add(history.makgeolliConsumption.sum().multiply(MAKGEOLLI_ALCOHOL))
                )
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(thisMonthStart, thisMonthEnd)))
                .fetchOne();

        // 지난 달 총 알코올 섭취량 계산
        Float lastMonthTotalAlcohol = queryFactory
                .select(
                        history.sojuConsumption.sum().multiply(SOJU_ALCOHOL)
                                .add(history.wineConsumption.sum().multiply(WINE_ALCOHOL))
                                .add(history.beerConsumption.sum().multiply(BEER_ALCOHOL))
                                .add(history.makgeolliConsumption.sum().multiply(MAKGEOLLI_ALCOHOL))
                )
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(lastMonthStart, lastMonthEnd)))
                .fetchOne();

        // Null 값 처리를 위해 0으로 초기화
        thisMonthDrinkCount = thisMonthDrinkCount == null ? 0L : thisMonthDrinkCount;
        lastMonthDrinkCount = lastMonthDrinkCount == null ? 0L : lastMonthDrinkCount;
        thisMonthTotalAlcohol = thisMonthTotalAlcohol == null ? 0f : thisMonthTotalAlcohol;
        lastMonthTotalAlcohol = lastMonthTotalAlcohol == null ? 0f : lastMonthTotalAlcohol;

        // 차이 계산
        Long drinkCountDifference = thisMonthDrinkCount - lastMonthDrinkCount;
        Float totalAlcoholDifference = thisMonthTotalAlcohol - lastMonthTotalAlcohol;

        return UserResponse.MonthlyStatisticsCountsDto.builder()
                .drinkCount(thisMonthDrinkCount)
                .drinkCountDiff(drinkCountDifference)
                .totalAlcohol(thisMonthTotalAlcohol)
                .totalAlcoholDiff(totalAlcoholDifference)
                .build();
    }

    @Override
    public UserResponse.WeeklyStatisticsAveragesDto findWeeklyAverage(User user) {
        QHistory history = QHistory.history;

        // 현재 날짜와 최근 3개월 시작일 및 종료일 계산
        LocalDate now = LocalDate.now();
        LocalDate end = now.with(java.time.DayOfWeek.SUNDAY);
        LocalDate start = now.withDayOfMonth(1).minusMonths(2);

        // 최근 3개월 동안의 총 일 수 계산
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(start, end.plusDays(1));

        // 음주 빈도와 평균 음주량 계산
        Long totalDrinkCount = queryFactory
                .select(history.date.count())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        Double sojuAvgDouble = queryFactory
                .select(history.sojuConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        Double wineAvgDouble = queryFactory
                .select(history.wineConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        Double beerAvgDouble = queryFactory
                .select(history.beerConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        Double makgeolliAvgDouble = queryFactory
                .select(history.makgeolliConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(start, end)))
                .fetchOne();

        // Null 값 처리 및 변환
        totalDrinkCount = totalDrinkCount == null ? 0L : totalDrinkCount;
        Float sojuAverage = sojuAvgDouble == null ? 0f : sojuAvgDouble.floatValue();
        Float wineAverage = wineAvgDouble == null ? 0f : wineAvgDouble.floatValue();
        Float beerAverage = beerAvgDouble == null ? 0f : beerAvgDouble.floatValue();
        Float makgeolliAverage = makgeolliAvgDouble == null ? 0f : makgeolliAvgDouble.floatValue();

        log.info(String.valueOf(totalDrinkCount)+ " " + String.valueOf(totalDays));

        // 최근 3개월 동안의 주 평균 음주 빈도 계산
        Float frequency = totalDays > 0 ? (float) totalDrinkCount / totalDays : 0f;

        return UserResponse.WeeklyStatisticsAveragesDto.builder()
                .averageFrequency(frequency)
                .sojuAverage(sojuAverage)
                .wineAverage(wineAverage)
                .beerAverage(beerAverage)
                .makgeolliAverage(makgeolliAverage)
                .build();
    }

    @Override
    public UserResponse.MonthlyStatisticsAveragesDto findMonthlyAverage(User user) {
        QHistory history = QHistory.history;

        // 현재 날짜와 최근 3개월 시작일 및 종료일 계산
        LocalDate now = LocalDate.now();
        LocalDate today = now.with(java.time.DayOfWeek.SUNDAY);
        LocalDate threeMonthsAgo = today.minusMonths(3);

        // 월 단위로 최근 3개월 동안의 시작일과 종료일 계산
        LocalDate startOfMonth = threeMonthsAgo.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        // 최근 3개월 동안의 총 월 수 계산
        long totalMonths = ChronoUnit.MONTHS.between(startOfMonth, endOfMonth) + 1;

        // 음주 빈도와 평균 음주량 계산
        Long totalDrinkCount = queryFactory
                .select(history.date.countDistinct())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startOfMonth, endOfMonth))
                        .and(history.sojuConsumption.gt(0f)
                                .or(history.wineConsumption.gt(0f))
                                .or(history.beerConsumption.gt(0f))
                                .or(history.makgeolliConsumption.gt(0f))))
                .fetchOne();

        Double sojuAvgDouble = queryFactory
                .select(history.sojuConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startOfMonth, endOfMonth)))
                .fetchOne();

        Double wineAvgDouble = queryFactory
                .select(history.wineConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startOfMonth, endOfMonth)))
                .fetchOne();

        Double beerAvgDouble = queryFactory
                .select(history.beerConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startOfMonth, endOfMonth)))
                .fetchOne();

        Double makgeolliAvgDouble = queryFactory
                .select(history.makgeolliConsumption.avg())
                .from(history)
                .where(history.user.eq(user)
                        .and(history.date.between(startOfMonth, endOfMonth)))
                .fetchOne();

        // Null 값 처리 및 변환
        totalDrinkCount = totalDrinkCount == null ? 0L : totalDrinkCount;
        Float sojuAverage = sojuAvgDouble == null ? 0f : sojuAvgDouble.floatValue();
        Float wineAverage = wineAvgDouble == null ? 0f : wineAvgDouble.floatValue();
        Float beerAverage = beerAvgDouble == null ? 0f : beerAvgDouble.floatValue();
        Float makgeolliAverage = makgeolliAvgDouble == null ? 0f : makgeolliAvgDouble.floatValue();

        // 최근 3개월 동안의 월 평균 음주 빈도 계산
        Long averageCount = totalDrinkCount / totalMonths;

        return UserResponse.MonthlyStatisticsAveragesDto.builder()
                .averageCount(averageCount)
                .sojuAverage(sojuAverage)
                .wineAverage(wineAverage)
                .beerAverage(beerAverage)
                .makgeolliAverage(makgeolliAverage)
                .build();
    }
}
