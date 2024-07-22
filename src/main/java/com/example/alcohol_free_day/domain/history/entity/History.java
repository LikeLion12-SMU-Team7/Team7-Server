package com.example.alcohol_free_day.domain.history.entity;

import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Date date;

    private Float sojuConsumption;
    private Float wineConsumption;
    private Float beerConsumption;
    private Float makgeolliConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
