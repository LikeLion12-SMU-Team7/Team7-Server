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
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 프로필 조회", description = "유저 프로필 정보를 조회합니다.")
    @GetMapping()
    public ApiResponse<UserResponse> getProfile(
            @AuthenticationPrincipal User user
            ) {
        return ApiResponse.onSuccess(userService.getProfile(user));
    }

    @Operation(summary = "유저 프로필 수정", description = "유저 프로필 정보를 수정합니다.")
    @PutMapping()
    public ApiResponse<UserResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UserRequest request
            ) {
        return ApiResponse.onSuccess(userService.updateProfile(user, request));
    }
}