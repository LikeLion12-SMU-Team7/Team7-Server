package com.example.alcohol_free_day.domain.auth.dto;

import com.example.alcohol_free_day.domain.user.entity.enums.Gender;

import java.time.LocalDate;

public record JoinUserRequest(
        String email,
        String password,
        String nickname,
        String gender,
        LocalDate birthDate,
        Float weight,
        Float sojuAmount
) {
}
