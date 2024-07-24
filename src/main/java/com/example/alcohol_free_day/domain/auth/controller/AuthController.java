package com.example.alcohol_free_day.domain.auth.controller;

import com.example.alcohol_free_day.domain.auth.dto.AuthRequest;
import com.example.alcohol_free_day.domain.auth.dto.AuthResponse;
import com.example.alcohol_free_day.domain.auth.dto.JoinUserRequest;
import com.example.alcohol_free_day.domain.auth.service.AuthService;
import com.example.alcohol_free_day.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody JoinUserRequest request) {
        authService.register(request);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        return ApiResponse.onSuccess(authService.authenticate(request));
    }

    @Operation(summary = "리프레시 토큰 발급")
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}
