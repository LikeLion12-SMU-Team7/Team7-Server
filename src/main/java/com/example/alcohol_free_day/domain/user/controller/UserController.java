package com.example.alcohol_free_day.domain.user.controller;

import com.example.alcohol_free_day.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping
    public ApiResponse<String> test() {
        return ApiResponse.onSuccess("test controller!");
    }
}
