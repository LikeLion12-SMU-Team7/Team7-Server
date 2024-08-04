package com.example.alcohol_free_day.domain.user.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record UserResponse(
        String email,
        String nickname,
        String gender,
        Float sojuAmount,
        LocalDate birthDate,
        Float weight
) {
    @Builder
    public record HomeDto(
        List<Calendar> calendarList,
        // 연속 기록한 날
        Long continuousRecordDay,
        // 연속 술 없는 날
        Long alcoholFreeDay,
        HomeUserInfoDto info
    ) {}

    @Builder
    public record HistoryDto(
            List<Calendar> calendarList,
            String memoryPreview
    ) {}

    @Builder
    public record WeeklyStatisticsComparedDto(
            // 주종별 이번 주 총 음주량
            Float weeklySojuCount,
            Float weeklyWineCount,
            Float weeklyBeerCount,
            Float weeklyMakgeolliCount,
            // 주종별 지난 주 대비 음주량
            Float sojuDifference,
            Float wineDifference,
            Float beerDifference,
            Float makgeolliDifference
    ) {}

    @Builder
    public record WeeklyStatisticsCountsDto(
            // 마신 횟수
            Long drinkCount,
            // 지난주 대비 마신 횟수
            Long drinkCountDiff,
            // 총 음주량 (총 섭취 알콜 g)
            Float totalAlcohol,
            // 지난 주 대비 음주량 차이
            Float totalAlcoholDiff
    ) {}

    @Builder
    public record WeeklyStatisticsAveragesDto(
            // 최근 3개월 한 주 평균 음주 빈도
            Float averageFrequency,
            // 최근 3개월 주종별 한 주 평균 음주량
            Float sojuAverage,
            Float wineAverage,
            Float beerAverage,
            Float makgeolliAverage
    ) {}

    @Builder
    public record MonthlyStatisticsComparedDto(
            // 주종별 이번 달 총 음주량
            Float monthlySojuCount,
            Float monthlyWineCount,
            Float monthlyBeerCount,
            Float monthlyMakgeolliCount,
            // 주종별 지난 달 대비 음주량
            Float sojuDifference,
            Float wineDifference,
            Float beerDifference,
            Float makgeolliDifference
            ) {}

    @Builder
    public record MonthlyStatisticsCountsDto(
            // 마신 횟수
            Long drinkCount,
            // 지난달 대비 마신 횟수
            Long drinkCountDiff,
            // 총 음주량  (총 섭취 알콜 g)
            Float totalAlcohol,
            // 지난달 대비 음주량 차이
            Float totalAlcoholDiff
    ) {}

    @Builder
    public record MonthlyStatisticsAveragesDto(
            // 최근 3개월 한 달 평균 음주 빈도
            Long averageCount,
            // 최근 3개월 주종별 한 달 평균 음주량
            Float sojuAverage,
            Float wineAverage,
            Float beerAverage,
            Float makgeolliAverage
    ) {}

    @Builder
    public record HomeUserInfoDto(
            // 나의 성별, 연령 적정량 (한 달 음주량)
            Float monthlyConsumption,
            // 이번 달 주류 섭취 칼로리
            Float monthlyCalorie,
            // 이번 달 예상 지출액
            Float expectedCost,
            // 보유 포인트
            Long point
    ) {}

    @Builder
    public record Calendar(
            LocalDate date,
            Float totalConsumption
    ) {}
}
