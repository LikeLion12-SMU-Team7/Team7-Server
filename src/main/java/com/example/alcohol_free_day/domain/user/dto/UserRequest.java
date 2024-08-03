package com.example.alcohol_free_day.domain.user.dto;

import java.time.LocalDate;
import java.util.Date;

public record UserRequest(
    String nickname,
    String gender,
    Float sojuAmount,
    LocalDate birthDate,
    Float weight
) {

    public record History(
            Date date,
            Float sojuConsumption,
            Float wineConsumption,
            Float beerConsumption,
            Float makgeolliConsumption
    ) {}

    public record NoneDrinkHistory(
            Date date
    ) {}
}
