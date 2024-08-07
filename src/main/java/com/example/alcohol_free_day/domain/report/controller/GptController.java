package com.example.alcohol_free_day.domain.report.controller;

import com.example.alcohol_free_day.domain.report.dto.ReportResponse;
import com.example.alcohol_free_day.domain.report.service.GptService;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gpt")
@CrossOrigin("*")
public class GptController {

    private final GptService gptService;

    @Operation(summary = "ChatGpt에 보고서 요청", description = "요청한 유저 정보 기반으로 보고서 조회, 저장, 유저 포인트 차감")
    @PostMapping("/")
    public ApiResponse<ReportResponse> getAssistantMsg(
            @AuthenticationPrincipal User user) throws JsonProcessingException {
        if (user.getPoint() < 10) {
            return ApiResponse.onSuccess(gptService.notEnoughPoint(user));
        }
        return ApiResponse.onSuccess(gptService.getAssistantMsg(user));
    }
}
