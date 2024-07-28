package com.example.alcohol_free_day.domain.user.controller;

import com.example.alcohol_free_day.domain.user.dto.UserRequest;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.service.UserService;
import com.example.alcohol_free_day.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class DashboardController {

    private final UserService userService;

    @Operation(summary = "홈 대시보드 조회", description = "홈 화면을 조회합니다.")
    @GetMapping("/home")
    public ApiResponse<UserResponse.Home> getHomeDashboardInfo(
            @AuthenticationPrincipal User user
            ) {
        return ApiResponse.onSuccess(userService.getHomeDashboardInfo(user));
    }

    @Operation(summary = "음주 기록 대시보드 조회", description = "음주 기록 화면을 조회합니다.")
    @GetMapping("/history")
    public ApiResponse<UserResponse.History> getHistoryDashboardInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getHistoryDashboardInfo(user));
    }

    @Operation(summary = "음주 기록 대시보드 작성", description = "음주 기록을 작성합니다.")
    @PostMapping("/history")
    public ApiResponse<?> recordHistory(
            @AuthenticationPrincipal User user,
            @RequestBody UserRequest.History request
            ) {
        return ApiResponse.onSuccess(userService.createHistory(user, request));
    }

    @Operation(summary = "주간 음주 통계 대시보드 조회", description = "주간 음주 통계 화면을 조회합니다.")
    @GetMapping("/weekly-statistics")
    public ApiResponse<UserResponse.WeeklyStatistics> getWeeklyStatisticsInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getWeeklyStatisticsInfo(user));
    }

    @Operation(summary = "월간 음주 통계 대시보드 조회", description = "월간 음주 통계 화면을 조회합니다.")
    @GetMapping("/monthly-statistics")
    public ApiResponse<UserResponse.MonthlyStatistics> getMonthlyStatisticsInfo(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(userService.getMonthlyStatisticsInfo(user));
    }
}
