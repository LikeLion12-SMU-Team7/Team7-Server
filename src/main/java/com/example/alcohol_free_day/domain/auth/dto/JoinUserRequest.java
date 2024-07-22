package com.example.alcohol_free_day.domain.auth.dto;

public record JoinUserRequest(
        String email,
        String password
) {
}
