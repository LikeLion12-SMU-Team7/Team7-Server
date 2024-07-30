package com.example.alcohol_free_day.domain.report.service;

import com.example.alcohol_free_day.domain.history.service.HistoryService;
import com.example.alcohol_free_day.domain.report.entity.Report;
import com.example.alcohol_free_day.domain.report.repository.ReportRepository;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class GptService {
    @Value("${openai.api.key}")
    private String apiKey;

    private final ReportRepository reportRepository;
    private final HistoryService historyService;
    private final UserService userService;

    public JsonNode callChatGpt(String userMsg) throws JsonProcessingException {
        final String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", "gpt-4o");  // 모델 이름을 gpt-3.5-turbo로 변경

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userMsg);
        messages.add(userMessage);

        Map<String, String> assistantMessage = new HashMap<>();
        assistantMessage.put("role", "system");
        assistantMessage.put("content", "너는 친절한 AI야");
        messages.add(assistantMessage);

        bodyMap.put("messages", messages);

        String body = objectMapper.writeValueAsString(bodyMap);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return objectMapper.readTree(response.getBody());
    }

    public String getAssistantMsg(User user) throws JsonProcessingException {

        // 포인트 차감
        userService.chargePoints(user);

        // 프롬프트 생성
        Float totalConsumption = historyService.calculateAlcoholConsumption(user);

        StringBuilder builder = new StringBuilder();
        builder.append("내가 취할 정도의 주량은 소주 " + user.getSojuAmount() + "병이고, ");
        builder.append("내가 근 일주일간 섭취한 알코올 총량은 " + totalConsumption + "g야. ");
        builder.append("적절한 음주 습관을 위해 어떻게 조절하면 좋을지 계획을 짜줘. 그리고 \"~니다.\"로 끝나게 답변 해줘.\n\n");

        JsonNode jsonNode = callChatGpt(builder.toString());
        String gptResponse = jsonNode.path("choices").get(0).path("message").path("content").asText();

        Report report = Report.builder()
                .content(gptResponse)
                .user(user)
                .build();
        reportRepository.save(report);

        return gptResponse;
    }
}
