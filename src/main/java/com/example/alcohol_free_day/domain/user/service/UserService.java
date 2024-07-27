package com.example.alcohol_free_day.domain.user.service;

import com.example.alcohol_free_day.domain.history.converter.HistoryConverter;
import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.history.repository.HistoryRepository;
import com.example.alcohol_free_day.domain.user.dto.UserRequest;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.repository.UserRepository;
import com.example.alcohol_free_day.global.common.code.status.ErrorStatus;
import com.example.alcohol_free_day.global.common.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public UserResponse getProfile(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(String.valueOf(user.getGender()))
                .birthDate(user.getBirthDate())
                .weight(user.getWeight())
                .build();
    }

    public UserResponse updateProfile(User user, UserRequest request) {
        user.updateProfile(request);
        User savedUser = userRepository.save(user);
        return UserResponse.builder()
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .gender(String.valueOf(savedUser.getGender()))
                .birthDate(savedUser.getBirthDate())
                .weight(savedUser.getWeight())
                .build();
    }

    public void chargePoints(User user) {
        if (user.getPoint() < 10) {
            throw new GeneralException(ErrorStatus._NOT_ENOUGH_POINT);
        }
        user.chargePoint();
        userRepository.save(user);
    }

    public UserResponse.Home getHomeDashboardInfo(User user) {
        List<History> historyList = historyRepository.findAllByUser(user);
        List<UserResponse.Calendar> calendarList = historyList.stream()
                .map(HistoryConverter::toCalendarDto).toList();
        UserResponse.HomeUserInfo homeInfo = userRepository.findHomeInfo(user);

        return UserResponse.Home.builder()
                .calendarList(calendarList)
                .info(homeInfo)
                .build();
    }

    public UserResponse.History getHistoryDashboardInfo(User user) {
        List<History> historyList = historyRepository.findAllByUser(user);
        List<UserResponse.Calendar> calendarList = historyList.stream()
                .map(HistoryConverter::toCalendarDto).toList();
        UserResponse.HistoryUserInfo historyInfo = userRepository.findHistoryInfo(user);

        return UserResponse.History.builder()
                .calendarList(calendarList)
                .info(historyInfo)
                .build();
    }

    public UserResponse.WeeklyStatistics getWeeklyStatisticsInfo(User user) {
        return userRepository.findWeeklyStatistics(user);
    }

    public UserResponse.MonthlyStatistics getMonthlyStatisticsInfo(User user) {
        return userRepository.findMonthlyStatistics(user);
    }

    public String createHistory(User user, UserRequest.History request) {
        History history = History.builder()
                .date(request.date())
                .sojuConsumption(request.sojuConsumption())
                .wineConsumption(request.wineConsumption())
                .beerConsumption(request.beerConsumption())
                .makgeolliConsumption(request.makgeolliConsumption())
                .user(user)
                .build();

        historyRepository.save(history);
        return "기록 완료";
    }
}
