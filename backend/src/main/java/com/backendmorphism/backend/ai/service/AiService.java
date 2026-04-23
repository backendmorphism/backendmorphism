package com.backendmorphism.backend.ai.service;

import com.backendmorphism.backend.ai.dto.AiRequest;
import com.backendmorphism.backend.ai.dto.AiResponse;
import com.backendmorphism.backend.ai.provider.AiProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Autowired
    private AiProvider aiProvider;

    public String generateResponse(
            String systemPrompt,
            String userPrompt
    ) {

        AiRequest request =
                AiRequest.builder()
                        .systemPrompt(systemPrompt)
                        .userPrompt(userPrompt)
                        .build();

        AiResponse response =
                aiProvider.generateResponse(request);

        return response.getContent();
    }
}