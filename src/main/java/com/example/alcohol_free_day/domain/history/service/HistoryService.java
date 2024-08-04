package com.example.alcohol_free_day.domain.history.service;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.history.repository.HistoryRepository;
import com.example.alcohol_free_day.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public Float calculateAlcoholConsumption(User user) {
        // 현재 날짜 기준으로 일주일 전 날짜 계산
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        // 최근 일주일 간의 History 데이터 조회
        List<History> histories = historyRepository.findByUserAndDateAfter(user, oneWeekAgo);

        // 각 알코올 종류의 병 당 알코올 양 (g)
        final float SOJU_ALCOHOL = 60.84f; // 소주 360ml, 알코올 도수 16.9%
        final float WINE_ALCOHOL = 101.25f; // 와인 750ml, 알코올 도수 13.5%
        final float BEER_ALCOHOL = 22.5f; // 맥주 500ml, 알코올 도수 4.5%
        final float MAKGEOLLI_ALCOHOL = 45f; // 막걸리 750ml, 알코올 도수 6%

        // 각 알코올 종류의 섭취량 합산
        Float totalConsumption = histories.stream()
                .map(history ->
                        history.getSojuConsumption() * SOJU_ALCOHOL +
                                history.getWineConsumption() * WINE_ALCOHOL +
                                history.getBeerConsumption() * BEER_ALCOHOL +
                                history.getMakgeolliConsumption() * MAKGEOLLI_ALCOHOL)
                .reduce(0f, Float::sum);

        return totalConsumption;
    }
}
