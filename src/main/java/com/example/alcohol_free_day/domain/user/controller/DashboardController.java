package com.example.alcohol_free_day.domain.user.controller;

import com.example.alcohol_free_day.domain.history.dto.HistoryResponse;
import com.example.alcohol_free_day.domain.user.dto.UserRequest;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.service.UserService;
import com.example.alcohol_free_day.global.annotation.CheckMonth;
import com.example.alcohol_free_day.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class DashboardController {

    private final UserService userService;

    @Operation(summary = "홈 대시 보드 조회", description = "홈 화면을 조회합니다.")
    @GetMapping("/home")
    public ApiResponse<UserResponse.HomeDto> getHomeDashboardInfo(
            @AuthenticationPrincipal User user,
            @CheckMonth @RequestParam(name = "month") Integer month
            ) {
        return ApiResponse.onSuccess(userService.getHomeDashboardInfo(user, month));
    }

    @Operation(summary = "음주 기록 대시 보드 조회", description = "음주 기록 화면을 조회합니다.")
    @GetMapping("/history")
    public ApiResponse<UserResponse.HistoryDto> getHistoryDashboardInfo(
            @AuthenticationPrincipal User user,
            @CheckMonth @RequestParam(name = "month") Integer month
    ) {
        return ApiResponse.onSuccess(userService.getHistoryDashboardInfo(user, month));
    }

    @Operation(summary = "음주 기록 대시 보드 조회: 주종 별, 날짜 별 마신 양 조회", description = "주간, 월간 공통 API, 오늘 기록한 음주 내역을 조회합니다. 주종 별로 조회합니다. 조회하려는 날짜를 QueryString으로 입력해야 합니다. 만약 조회하려는 날짜 입력 안하면 그냥 오늘꺼 보내줌")
    @GetMapping("/history/today")
    public ApiResponse<HistoryResponse.TodayDto> getTodayHistory(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "date", required = false) LocalDate date
            ) {
        return ApiResponse.onSuccess(userService.getTodayHistory(user, date));
    }

    @Operation(summary = "음주 기록 대시 보드 작성", description = "음주 기록을 작성합니다.")
    @PostMapping("/history")
    public ApiResponse<String> recordHistory(
            @AuthenticationPrincipal User user,
            @RequestBody UserRequest.HistoryDto request
            ) {
        return ApiResponse.onSuccess(userService.createHistory(user, request));
    }

    @Operation(summary = "음주 기록 대시 보드: 이 날 안 마셨어요 버튼", description = "한 잔도 마시지 않은 음주 기록을 추가합니다.")
    @PostMapping("/history/none-drink")
    public ApiResponse<String> noneDrinkToday(
            @AuthenticationPrincipal User user,
            @RequestBody UserRequest.NoneDrinkHistoryDto request
    ) {
        return ApiResponse.onSuccess(userService.createNoneDrinkHistory(user, request));
    }

    @Operation(summary = "주간 음주 통계 대시 보드: 주종 별 지난 주 대비 음주량")
    @GetMapping("/weekly-statistics/compared")
    public ApiResponse<UserResponse.WeeklyStatisticsComparedDto> getWeeklyStatisticsInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getWeeklyCompared(user));
    }

    @Operation(summary = "주간 음주 통계 대시 보드: 마신 횟수, 총 음주량(g 단위) 조회")
    @GetMapping("/weekly-statistics/count")
    public ApiResponse<UserResponse.WeeklyStatisticsCountsDto> getWeeklyStatisticsCounts(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getWeeklyCount(user));
    }

    @Operation(summary = "주간 음주 통계 대시 보드: 최근 3개월간 한 주 평균 음주 빈도, 주종별 한 주 평균 음주량")
    @GetMapping("/weekly-statistics/average")
    public ApiResponse<UserResponse.WeeklyStatisticsAveragesDto> getWeeklyStatisticsAverages(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getWeeklyAverage(user));
    }

    @Operation(summary = "월간 음주 통계 대시 보드: 주종 별 지난 달 대비 음주량")
    @GetMapping("/monthly-statistics/compared")
    public ApiResponse<UserResponse.MonthlyStatisticsComparedDto> getMonthlyStatisticsInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getMonthlyCompared(user));
    }

    @Operation(summary = "월간 음주 통계 대시 보드: 마신 횟수, 총 음주량(g 단위) 조회")
    @GetMapping("/monthly-statistics/count")
    public ApiResponse<UserResponse.MonthlyStatisticsCountsDto> getMonthlyStatisticsCounts(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getMonthlyCount(user));
    }

    @Operation(summary = "월간 음주 통계 대시 보드: 최근 3개월간 한 달 평균 음주량, 주종별 한 달 평균 음주량")
    @GetMapping("/monthly-statistics/average")
    public ApiResponse<UserResponse.MonthlyStatisticsAveragesDto> getMonthlyStatisticsAverages(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getMonthlyAverage(user));
    }
}
