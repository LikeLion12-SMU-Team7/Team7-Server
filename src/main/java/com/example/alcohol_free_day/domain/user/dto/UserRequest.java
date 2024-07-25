package com.example.alcohol_free_day.domain.user.dto;

import java.time.LocalDate;

public record UserRequest(
    String nickname,
    String gender,
    LocalDate birthDate,
    Float weight
) {
}
