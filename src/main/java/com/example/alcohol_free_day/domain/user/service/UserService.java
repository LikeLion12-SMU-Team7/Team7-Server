package com.example.alcohol_free_day.domain.user.service;

import com.example.alcohol_free_day.domain.history.converter.HistoryConverter;
import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.history.repository.HistoryRepository;
import com.example.alcohol_free_day.domain.memory.entity.Memory;
import com.example.alcohol_free_day.domain.memory.repository.MemoryRepository;
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
    private final MemoryRepository memoryRepository;

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

    // TODO 월 단위로 페이지네이션 추가
    public UserResponse.Home getHomeDashboardInfo(User user) {
        List<History> historyList = historyRepository.findAllByUser(user);
        List<UserResponse.Calendar> calendarList = historyList.stream()
                .map(HistoryConverter::toCalendarDto).toList();

        UserResponse.HomeUserInfo homeInfo = userRepository.findHomeInfo(user);

        return UserResponse.Home.builder()
                .calendarList(calendarList)
                .alcoholFreeDay(calculateAlcoholFreeDays(historyList))
                .info(homeInfo)
                .build();
    }

    public UserResponse.History getHistoryDashboardInfo(User user) {
        List<History> historyList = historyRepository.findAllByUser(user);
        List<UserResponse.Calendar> calendarList = historyList.stream()
                .map(HistoryConverter::toCalendarDto).toList();
        Memory memory = memoryRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElse(null);


        return UserResponse.History.builder()
                .calendarList(calendarList)
                .memoryPreview(memory.getContent())
                .build();
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

    public String createNoneDrinkHistory(User user, UserRequest.NoneDrinkHistory request) {
        History history = History.builder()
                .date(request.date())
                .sojuConsumption(0f)
                .wineConsumption(0f)
                .beerConsumption(0f)
                .makgeolliConsumption(0f)
                .user(user)
                .build();

        historyRepository.save(history);
        return "기록 완료";
    }

    // TODO 오류 수정
    public UserResponse.WeeklyStatisticsCompared getWeeklyCompared(User user) {
        return historyRepository.findWeeklyCompared(user);
    }

    // TODO 오류 수정
    public UserResponse.WeeklyStatisticsCounts getWeeklyCount(User user) {
        return historyRepository.findWeeklyCount(user);
    }

    public UserResponse.WeeklyStatisticsAverages getWeeklyAverage(User user) {
        return historyRepository.findWeeklyAverage(user);
    }

    public UserResponse.MonthlyStatisticsCompared getMonthlyCompared(User user) {
        return historyRepository.findMonthlyCompared(user);
    }

    public UserResponse.MonthlyStatisticsCounts getMonthlyCount(User user) {
        return historyRepository.findMonthlyCount(user);
    }

    public UserResponse.MonthlyStatisticsAverages getMonthlyAverage(User user) {
        return historyRepository.findMonthlyAverage(user);
    }

    private Long calculateAlcoholFreeDays(List<History> histories) {
        long maxStreak = 0L;
        long currentStreak = 0L;

        for (History history : histories) {
            if (history.getSojuConsumption() == 0 && history.getWineConsumption() == 0 &&
                    history.getBeerConsumption() == 0 && history.getMakgeolliConsumption() == 0) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0L;
            }
        }

        return maxStreak;
    }
}
