package com.backendmorphism.backend.ai.provider;

import com.backendmorphism.backend.ai.dto.AiRequest;
import com.backendmorphism.backend.ai.dto.AiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Primary
@Slf4j
public class OpenAiProvider implements AiProvider {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient =
            WebClient.builder()
                    .baseUrl("https://api.openai.com/v1")
                    .build();

    @Override
    public AiResponse generateResponse(
            AiRequest request
    ) {

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", new Object[]{
                        Map.of(
                                "role", "system",
                                "content", request.getSystemPrompt()
                        ),
                        Map.of(
                                "role", "user",
                                "content", request.getUserPrompt()
                        )
                },
                "temperature", 0.7
        );

        try {

            Map response = webClient.post()
                    .uri("/chat/completions")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + apiKey
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String content =
                    (String)
                            ((Map)
                                    ((Map)
                                            ((java.util.List)
                                                    response.get("choices"))
                                                    .get(0))
                                            .get("message"))
                                    .get("content");

            return AiResponse.builder()
                    .content(content)
                    .build();

        } catch (Exception ex) {

            log.error("OpenAI API failed", ex);

            return buildFallbackResponse(request);
        }
    }

    private AiResponse buildFallbackResponse(
            AiRequest request
    ) {

        String fallbackJson;

        // summary prompt fallback
        if (request.getUserPrompt()
                .contains("overallScore")) {

            fallbackJson = """
                {
                  "overallScore": 6,
                  "technicalScore": 6,
                  "communicationScore": 6,
                  "hiringRecommendation": "Hire",
                  "strengths": "Basic technical understanding.",
                  "weaknesses": "Needs deeper practical knowledge.",
                  "improvementAreas": "Practice more real-world interview scenarios.",
                  "overallFeedback": "AI evaluation temporarily unavailable."
                }
                """;

        } else {

            // interview evaluation fallback
            fallbackJson = """
                {
                  "score": 5,
                  "feedback": "AI evaluation temporarily unavailable.",
                  "nextQuestion": "Explain dependency injection in Spring Boot."
                }
                """;
        }

        return AiResponse.builder()
                .content(fallbackJson)
                .build();
    }
}