package com.backendmorphism.backend.ai.provider;

import com.backendmorphism.backend.ai.dto.AiRequest;
import com.backendmorphism.backend.ai.dto.AiResponse;
import org.springframework.stereotype.Component;

@Component
public class MockAiProvider implements AiProvider {

    @Override
    public AiResponse generateResponse(
            AiRequest request
    ) {

        return AiResponse.builder()
                .content(
                        "Mock AI response for testing"
                )
                .build();
    }
}