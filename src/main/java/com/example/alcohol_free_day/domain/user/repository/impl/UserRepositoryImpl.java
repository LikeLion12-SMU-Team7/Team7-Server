package com.example.alcohol_free_day.domain.user.repository.impl;

import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.repository.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // TODO 쿼리문 짜기
    @Override
    public UserResponse.HomeUserInfo findHomeInfo(User user) {
        return null;
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
