package com.example.alcohol_free_day.domain.history.entity;

import com.example.alcohol_free_day.domain.user.dto.UserRequest;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    private LocalDate date;

    private Float sojuConsumption;
    private Float wineConsumption;
    private Float beerConsumption;
    private Float makgeolliConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateNoneDrink() {
        this.sojuConsumption = 0f;
        this.wineConsumption = 0f;
        this.beerConsumption = 0f;
        this.makgeolliConsumption = 0f;
    }

    public void update(UserRequest.History request, User user) {
        this.date = request.date();
        this.sojuConsumption = request.sojuConsumption();
        this.wineConsumption = request.wineConsumption();
        this.beerConsumption = request.beerConsumption();
        this.makgeolliConsumption = request.makgeolliConsumption();
        this.user = user;
    }
}
