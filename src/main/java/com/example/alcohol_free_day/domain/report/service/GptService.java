package com.example.alcohol_free_day.domain.report.service;

import com.example.alcohol_free_day.domain.history.service.HistoryService;
import com.example.alcohol_free_day.domain.report.dto.ReportResponse;
import com.example.alcohol_free_day.domain.report.entity.Report;
import com.example.alcohol_free_day.domain.report.repository.ReportRepository;
import com.example.alcohol_free_day.domain.user.entity.User;
import com.example.alcohol_free_day.domain.user.entity.enums.Gender;
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
        bodyMap.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userMsg);
        messages.add(userMessage);

        Map<String, String> assistantMessage = new HashMap<>();
        assistantMessage.put("role", "system");
        assistantMessage.put("content", "너는 사용자의 건강과 생활 습관에 맞추어 조언해주는 전문적인 의사 AI야. 정중한 어조로 사용자의 상황에 맞게 조언하는 능력이 뛰어나.");
        messages.add(assistantMessage);

        bodyMap.put("messages", messages);

        String body = objectMapper.writeValueAsString(bodyMap);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return objectMapper.readTree(response.getBody());
    }

    public ReportResponse getAssistantMsg(User user) throws JsonProcessingException {

        // 포인트 차감
        userService.chargePoints(user);

        // 프롬프트 생성
        Float totalConsumption = historyService.calculateAlcoholConsumption(user);

        StringBuilder builder = new StringBuilder();
        builder.append("뒤에 주어지는 사용자의 상황과 근 1주일간 섭취한 음주량에 집중해서 건강한 음주 습관을 형성하고 유지할 수 있도록 구체적으로 조언해줘. " +
                "예를 들어, 이번 주에 섭취한 알코올 양이 사용자의 나이와 체중, 성별을 고려했을 때 적정한 수준인지, 음주량을 줄이는 방법이나 대체 음료에 대한 팁을 함께 제공해 주시면 좋겠어. ");
        builder.append("사용자의 이름은 " + user.getNickname() + "이야. 사용자님이라고 부르지 말고 \"해당 이름\" + \"님\"으로 호칭을 불러줘. " +
                "사용자의 생년월일은 " + user.getBirthDate() + "이고, " +
                "사용자의 성별은 " + (user.getGender() == Gender.MALE ? "남성" : "여성") + "이야. " +
                "또 사용자의 체중은 " + user.getWeight() + "kg이고, " +
                "사용자가 취할 정도의 주량은 소주 " + user.getSojuAmount() + "병이고, ");
        builder.append("사용자가 근 일주일간 섭취한 알코올 총량은 " + totalConsumption + "g야. ");
        builder.append("\"~니다.\"로 끝나게 답변 해줘. 그리고 답변에 **은 사용 하지마.\n\n");

        JsonNode jsonNode = callChatGpt(builder.toString());
        String gptResponse = jsonNode.path("choices").get(0).path("message").path("content").asText();

        Report report = Report.builder()
                .content(gptResponse)
                .user(user)
                .build();
        reportRepository.save(report);

        return ReportResponse.builder()
                .report(gptResponse)
                .status("new")
                .build();
    }

    public ReportResponse notEnoughPoint(User user) {
        Report previousReport = reportRepository.findTopByUserOrderByCreatedAtDesc(user);

        return ReportResponse.builder()
                .report(previousReport.getContent())
                .status("previous")
                .build();
    }
}
