package com.example.alcohol_free_day.domain.report.controller;

import com.example.alcohol_free_day.domain.report.service.GptService;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gpt")
public class GptController {

    private final GptService gptService;

    @Autowired
    public GptController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/")
    public ApiResponse<?> getAssistantMsg(
            @AuthenticationPrincipal User user) throws JsonProcessingException {
        return ApiResponse.onSuccess(gptService.getAssistantMsg(user));
    }
}
