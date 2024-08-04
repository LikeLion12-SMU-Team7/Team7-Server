package com.example.alcohol_free_day.domain.user.service;

import com.example.alcohol_free_day.domain.history.converter.HistoryConverter;
import com.example.alcohol_free_day.domain.history.dto.HistoryResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final MemoryRepository memoryRepository;

    public UserResponse getProfile(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(String.valueOf(user.getGender()))
                .sojuAmount(user.getSojuAmount())
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
                .sojuAmount(savedUser.getSojuAmount())
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

    public UserResponse.HomeDto getHomeDashboardInfo(User user, Integer month) {
        List<History> calendarHistoryList = historyRepository.findAllByUserAndMonth(user, month);
        List<History> thisMonthHistoryList = historyRepository.findAllByUserAndMonth(user, LocalDate.now().getMonthValue());
        List<History> totalHistoryList = historyRepository.findAllByUser(user);
        List<UserResponse.Calendar> calendarList = calendarHistoryList.stream()
                .map(HistoryConverter::toCalendarDto).toList();

        UserResponse.HomeUserInfoDto homeInfo = userRepository.findHomeInfo(user);
        Long continuousRecord = calculateContinuousRecord(totalHistoryList);
        Long continuousAlcoholFreeDay = calculateAlcoholFreeDays(thisMonthHistoryList);

        if(continuousRecord < continuousAlcoholFreeDay) {
            return UserResponse.HomeDto.builder()
                    .calendarList(calendarList)
                    .continuousRecordDay(continuousRecord)
                    .alcoholFreeDay(continuousRecord)
                    .info(homeInfo)
                    .build();
        }

        return UserResponse.HomeDto.builder()
                .calendarList(calendarList)
                .continuousRecordDay(continuousRecord)
                .alcoholFreeDay(continuousAlcoholFreeDay)
                .info(homeInfo)
                .build();
    }

    public UserResponse.HistoryDto getHistoryDashboardInfo(User user, Integer month) {
        List<History> historyList = historyRepository.findAllByUserAndMonth(user, month);
        List<UserResponse.Calendar> calendarList = historyList.stream()
                .map(HistoryConverter::toCalendarDto).toList();
        Optional<Memory> memory = memoryRepository.findTopByUserOrderByCreatedAtDesc(user);

        return UserResponse.HistoryDto.builder()
                .calendarList(calendarList)
                .memoryPreview(memory.map(Memory::getContent).orElse(null))
                .build();
    }

    public HistoryResponse.TodayDto getTodayHistory(User user) {
        History history = historyRepository.findByUserAndDate(user, LocalDate.now());
        if(history == null) {
            return HistoryResponse.TodayDto.builder()
                    .todaySojuConsumption(0f)
                    .todayWineConsumption(0f)
                    .todayMakgeolliConsumption(0f)
                    .todayBeerConsumption(0f)
                    .build();
        }

        return HistoryConverter.toTodayDto(history);
    }

    public String createHistory(User user, UserRequest.HistoryDto request) {
        if (historyRepository.existsByUserAndDate(user, request.date())) {
            History history = historyRepository.findByUserAndDate(user, request.date());
            history.update(request, user);
            historyRepository.save(history);
            return "덮어 쓰기 완료";
        }

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

    public String createNoneDrinkHistory(User user, UserRequest.NoneDrinkHistoryDto request) {
        if (historyRepository.existsByUserAndDate(user, request.date())) {
            History history = historyRepository.findByUserAndDate(user, request.date());
            history.updateNoneDrink();
            historyRepository.save(history);
            return "덮어 쓰기 완료";
        }

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

    public UserResponse.WeeklyStatisticsComparedDto getWeeklyCompared(User user) {
        return historyRepository.findWeeklyCompared(user);
    }

    public UserResponse.WeeklyStatisticsCountsDto getWeeklyCount(User user) {
        return historyRepository.findWeeklyCount(user);
    }

    public UserResponse.WeeklyStatisticsAveragesDto getWeeklyAverage(User user) {
        return historyRepository.findWeeklyAverage(user);
    }

    public UserResponse.MonthlyStatisticsComparedDto getMonthlyCompared(User user) {
        return historyRepository.findMonthlyCompared(user);
    }

    public UserResponse.MonthlyStatisticsCountsDto getMonthlyCount(User user) {
        return historyRepository.findMonthlyCount(user);
    }

    public UserResponse.MonthlyStatisticsAveragesDto getMonthlyAverage(User user) {
        return historyRepository.findMonthlyAverage(user);
    }

    private Long calculateAlcoholFreeDays(List<History> histories) {
        LocalDate today = LocalDate.now();
        long currentStreak = 0L;

        // 오늘 날짜부터 시작하여 연속된 금주일 계산
        for (LocalDate date = today; !date.isBefore(today.minusDays(histories.size())); date = date.minusDays(1)) {
            boolean hasRecord = false;

            for (History history : histories) {
                if (history.getDate().isEqual(date)) {
                    hasRecord = true;

                    // 음주 여부 체크
                    if (history.getSojuConsumption() == 0 && history.getWineConsumption() == 0 &&
                            history.getBeerConsumption() == 0 && history.getMakgeolliConsumption() == 0) {
                        currentStreak++;
                    } else {
                        return currentStreak; // 술을 마신 날이면 연속성 리턴
                    }
                    break; // 해당 날짜의 기록을 찾았으므로 더 이상 반복할 필요 없음
                }
            }

            // 해당 날짜의 기록이 없을 경우
            if (!hasRecord) {
                return currentStreak; // 기록이 없으면 연속성 리턴
            }
        }

        return currentStreak; // 최대 streak 리턴
    }
    private Long calculateContinuousRecord(List<History> historyList) {
        // 오늘 날짜를 기준으로 시작
        LocalDate today = LocalDate.now().plusDays(1);
        long continuousDays = 0;

        // 날짜 정렬
        List<LocalDate> dates = historyList.stream()
                .map(History::getDate)
                .distinct()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        // 오늘 날짜부터 시작하여 연속된 날을 계산
        for (LocalDate date : dates) {
            if (date.equals(today)) {
                continuousDays++; // 오늘 날짜가 포함될 때
                today = today.minusDays(1); // 하루 전으로 이동
            } else if (date.equals(today.minusDays(1))) {
                continuousDays++; // 하루 전 날짜가 연속될 때
                today = today.minusDays(1); // 하루 전으로 이동
            } else {
                return continuousDays;
            }
        }

        return continuousDays;
    }

}
