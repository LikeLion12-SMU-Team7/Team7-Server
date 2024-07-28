package com.example.alcohol_free_day.domain.report.controller;

import com.example.alcohol_free_day.domain.report.service.GptService;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gpt")
@CrossOrigin("*")
public class GptController {

    private final GptService gptService;

    @Autowired
    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @Operation(summary = "ChatGpt에 보고서 요청", description = "요청한 유저 정보 기반으로 보고서 조회, 저장, 유저 포인트 차감")
    @PostMapping("/")
    public ApiResponse<?> getAssistantMsg(
            @AuthenticationPrincipal User user) throws JsonProcessingException {
        return ApiResponse.onSuccess(gptService.getAssistantMsg(user));
    }
}
