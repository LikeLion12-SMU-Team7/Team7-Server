package com.example.alcohol_free_day.security.jwt.dto;

public record JwtPair(
        String accessToken,
        String refreshToken
) {
}
