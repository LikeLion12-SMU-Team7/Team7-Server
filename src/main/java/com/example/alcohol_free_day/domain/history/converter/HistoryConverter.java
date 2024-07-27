package com.example.alcohol_free_day.domain.history.converter;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.user.dto.UserResponse;

public class HistoryConverter {

    public static UserResponse.Calendar toCalendarDto(History history) {
        return UserResponse.Calendar.builder()
                .when(history.getDate())
                .totalConsumption(history.getSojuConsumption()
                        + history.getWineConsumption()
                        + history.getBeerConsumption()
                        + history.getMakgeolliConsumption())
                .build();
    }
}
