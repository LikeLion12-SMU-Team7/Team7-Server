package com.example.alcohol_free_day.domain.user.controller;

import com.example.alcohol_free_day.domain.user.dto.UserRequest;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.service.UserService;
import com.example.alcohol_free_day.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ApiResponse<UserResponse> getProfile(
            @AuthenticationPrincipal User user
            ) {
        return ApiResponse.onSuccess(userService.getProfile(user));
    }

    @PutMapping()
    public ApiResponse<UserResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserRequest request
            ) {
        return ApiResponse.onSuccess(userService.updateProfile(user, request));
    }
}
