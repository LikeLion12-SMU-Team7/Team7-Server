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

    @Operation(summary = "회원가입 기능", description = "gender 값: MALE or FEMALE")
    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody JoinUserRequest request) {
        authService.register(request);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @Operation(summary = "이메일 중복 검사 버튼", description = "회원가입 과정에서 이메일 중복 검사를 진행합니다.\ntrue = 이미 존재하는 이메일, false = 가입 가능한 이메일")
    @GetMapping("/join/email-check/{email}")
    public ApiResponse<?> checkEmailExists(
            @PathVariable(name = "email") String email
    ) {
        return ApiResponse.onSuccess(authService.checkEmailExists(email));
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
