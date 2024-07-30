package com.example.alcohol_free_day.domain.history.repository;

import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepositoryCustom {
    UserResponse.WeeklyStatisticsCompared findWeeklyCompared(User user);

    UserResponse.MonthlyStatisticsCompared findMonthlyCompared(User user);

    UserResponse.WeeklyStatisticsCounts findWeeklyCount(User user);

    UserResponse.MonthlyStatisticsCounts findMonthlyCount(User user);

    UserResponse.WeeklyStatisticsAverages findWeeklyAverage(User user);

    UserResponse.MonthlyStatisticsAverages findMonthlyAverage(User user);
}
