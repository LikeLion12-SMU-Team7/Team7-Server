package com.example.alcohol_free_day.domain.history.converter;

import com.example.alcohol_free_day.domain.history.dto.HistoryResponse;
import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;

public class HistoryConverter {

    public static UserResponse.Calendar toCalendarDto(History history) {
        return UserResponse.Calendar.builder()
                .date(history.getDate())
                .totalConsumption(history.getSojuConsumption()
                        + history.getWineConsumption()
                        + history.getBeerConsumption()
                        + history.getMakgeolliConsumption())
                .build();
    }

    public static HistoryResponse.TodayDto toTodayDto(History history) {
        return HistoryResponse.TodayDto.builder()
                .todaySojuConsumption(history.getSojuConsumption() * 8)
                .todayWineConsumption(history.getWineConsumption() * 5)
                .todayBeerConsumption(history.getBeerConsumption())
                .todayMakgeolliConsumption(history.getMakgeolliConsumption() * 5)
                .build();
    }
}
