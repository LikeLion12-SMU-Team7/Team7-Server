package com.example.alcohol_free_day.domain.history.service;

import com.example.alcohol_free_day.domain.history.entity.History;
import com.example.alcohol_free_day.domain.history.repository.HistoryRepository;
import com.example.alcohol_free_day.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public Float calculateAlcoholConsumption(User user) {
        // 현재 날짜 기준으로 일주일 전 날짜 계산
        Date oneWeekAgo = new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000);

        // 최근 일주일 간의 History 데이터 조회
        List<History> histories = historyRepository.findByUserAndDateAfter(user, oneWeekAgo);

        // 각 알코올 종류의 병 당 알코올 양 (g)
        final float SOJU_ALCOHOL_CONTENT = 57.2f;
        final float WINE_ALCOHOL_CONTENT = 66.8f;
        final float BEER_ALCOHOL_CONTENT = 12.7f;
        final float MAKGEOLLI_ALCOHOL_CONTENT = 47.7f;

        // 각 알코올 종류의 섭취량 합산
        Float totalConsumption = histories.stream()
                .map(history ->
                        history.getSojuConsumption() * SOJU_ALCOHOL_CONTENT +
                                history.getWineConsumption() * WINE_ALCOHOL_CONTENT +
                                history.getBeerConsumption() * BEER_ALCOHOL_CONTENT +
                                history.getMakgeolliConsumption() * MAKGEOLLI_ALCOHOL_CONTENT)
                .reduce(0f, Float::sum);

        return totalConsumption;
    }
}
