package com.example.alcohol_free_day.domain.auth.dto;

import com.example.alcohol_free_day.security.jwt.dto.JwtPair;

import lombok.Builder;

@Builder
public record ReissueResponse(
        String accessToken,
        String refreshToken
) {

    public static ReissueResponse from(JwtPair jwtPair) {
        return ReissueResponse.builder()
                .accessToken(jwtPair.accessToken())
                .refreshToken(jwtPair.refreshToken())
                .build();
    }
}