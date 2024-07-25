package com.example.alcohol_free_day.domain.user.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponse(
        String email,
        String nickname,
        String gender,
        LocalDate birthDate,
        Float weight
) {
}
