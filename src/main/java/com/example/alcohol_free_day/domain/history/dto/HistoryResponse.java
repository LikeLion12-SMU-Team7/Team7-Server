package com.example.alcohol_free_day.domain.history.dto;

import lombok.Builder;

@Builder
public record HistoryResponse() {

    @Builder
    public record TodayDto(
            Float todaySojuConsumption,
            Float todayWineConsumption,
            Float todayBeerConsumption,
            Float todayMakgeolliConsumption
    ) {}
}
