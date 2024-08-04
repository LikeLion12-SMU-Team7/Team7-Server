package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepositoryCustom {
    UserResponse.WeeklyStatisticsComparedDto findWeeklyCompared(User user);

    UserResponse.MonthlyStatisticsComparedDto findMonthlyCompared(User user);

    UserResponse.WeeklyStatisticsCountsDto findWeeklyCount(User user);

    UserResponse.MonthlyStatisticsCountsDto findMonthlyCount(User user);

    UserResponse.WeeklyStatisticsAveragesDto findWeeklyAverage(User user);

    UserResponse.MonthlyStatisticsAveragesDto findMonthlyAverage(User user);
}
