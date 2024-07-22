package com.example.alcohol_free_day.domain.auth.controller;

import com.example.alcohol_free_day.domain.auth.dto.AuthRequest;
import com.example.alcohol_free_day.domain.auth.dto.JoinUserRequest;
import com.example.alcohol_free_day.domain.auth.dto.ReissueResponse;
import com.example.alcohol_free_day.domain.auth.service.AuthService;
import com.example.alcohol_free_day.global.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ApiResponse<String> join(@RequestBody JoinUserRequest request) {
        authService.register(request);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/login")
    public ApiResponse<ReissueResponse> login(@RequestBody AuthRequest request) {
        return ApiResponse.onSuccess(authService.authenticate(request));
    }

    @GetMapping("/reissue")
    public ApiResponse<ReissueResponse> reissueToken(@RequestHeader("RefreshToken") String refreshToken) {
        return ApiResponse.onSuccess(authService.reissueToken(refreshToken));
    }
}
