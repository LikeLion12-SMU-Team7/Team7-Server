package com.example.alcohol_free_day.domain.auth.dto;

public record AuthRequest(
        String email,
        String password
) {
}
