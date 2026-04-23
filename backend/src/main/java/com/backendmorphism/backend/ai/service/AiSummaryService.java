package com.backendmorphism.backend.ai.service;

import com.backendmorphism.backend.ai.prompt.InterviewPromptService;
import com.backendmorphism.backend.ai.util.AiJsonParser;
import com.backendmorphism.backend.interview.entity.InterviewInteraction;
import com.backendmorphism.backend.interview.entity.InterviewSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiSummaryService {

    @Autowired
    private AiService aiService;

    @Autowired
    private InterviewPromptService promptService;

    @Autowired
    private AiJsonParser aiJsonParser;

    public JsonNode generateSummary(
            InterviewSession session,
            List<InterviewInteraction> interactions
    ) {

        StringBuilder transcript =
                new StringBuilder();

        for (InterviewInteraction interaction : interactions) {

            if (interaction.getAiMessage() != null) {
                transcript.append("AI: ")
                        .append(interaction.getAiMessage())
                        .append("\n");
            }

            if (interaction.getUserMessage() != null) {
                transcript.append("USER: ")
                        .append(interaction.getUserMessage())
                        .append("\n");
            }
        }

        String prompt =
                promptService.buildInterviewSummaryPrompt(
                        session.getRole(),
                        session.getExperienceYears(),
                        transcript.toString()
                );

        String aiResponse =
                aiService.generateResponse(
                        "You are an expert technical interviewer.",
                        prompt
                );

        try {

            return aiJsonParser.parseJson(aiResponse);

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to parse interview summary"
            );
        }
    }
}