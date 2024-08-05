package com.example.alcohol_free_day.domain.report.controller;

import com.example.alcohol_free_day.domain.report.dto.ReportResponse;
import com.example.alcohol_free_day.domain.report.service.ReportService;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "가장 최근 보고서 조회", description = "발급받은 보고서를 조회합니다.")
    @GetMapping("/recent")
    public ApiResponse<ReportResponse.DetailDto> getRecentReport(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(reportService.getRecentReport(user));
    }

    @Operation(summary = "지금까지 사용자 보고서 전체 목록", description = "발급받은 보고서 목록을 조회합니다. 최신순으로 정렬")
    @GetMapping("")
    public ApiResponse<List<ReportResponse.PreviewDto>> getReportList(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.onSuccess(reportService.getReportList(user));
    }

    @Operation(summary = "사용자 보고서 상세 조회", description = "발급받은 보고서를 조회합니다.")
    @GetMapping("/{report_id}")
    public ApiResponse<ReportResponse.DetailDto> getReportDetail(
            @PathVariable(name = "report_id") Long reportId
    ) {
        return ApiResponse.onSuccess(reportService.getReportDetail(reportId));
    }
}
