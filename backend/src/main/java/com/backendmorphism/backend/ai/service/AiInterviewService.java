package com.backendmorphism.backend.ai.service;

import com.backendmorphism.backend.ai.dto.AiEvaluationResponse;
import com.backendmorphism.backend.ai.prompt.InterviewPromptService;
import com.backendmorphism.backend.ai.util.AiJsonParser;
import com.backendmorphism.backend.interview.entity.InterviewSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiInterviewService {

    @Autowired
    private AiService aiService;

    @Autowired
    private InterviewPromptService promptService;

    @Autowired
    private AiJsonParser aiJsonParser;

    public AiEvaluationResponse evaluateAnswer(
            InterviewSession session,
            String conversationHistory,
            String answer
    ) {

        String prompt =
                promptService.buildInterviewEvaluationPrompt(
                        session.getRole(),
                        session.getExperienceYears(),
                        conversationHistory,
                        answer
                );

        String aiResponse =
                aiService.generateResponse(
                        "You are an expert technical interviewer.",
                        prompt
                );

        try {

            JsonNode json =
                    aiJsonParser.parseJson(aiResponse);

            return AiEvaluationResponse.builder()
                    .score(json.get("score").asInt())
                    .feedback(json.get("feedback").asText())
                    .nextQuestion(
                            json.get("nextQuestion").asText()
                    )
                    .build();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to parse AI response"
            );
        }
    }
}