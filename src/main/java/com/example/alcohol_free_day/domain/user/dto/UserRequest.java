package com.example.alcohol_free_day.domain.user.dto;

import java.time.LocalDate;

public record UserRequest(
    String nickname,
    String gender,
    Float sojuAmount,
    LocalDate birthDate,
    Float weight
) {

    // 잔 단위
    public record HistoryDto(
            LocalDate date,
            Float sojuConsumption,
            Float wineConsumption,
            Float beerConsumption,
            Float makgeolliConsumption
    ) {}

    public record NoneDrinkHistoryDto(
            LocalDate date
    ) {}
}
