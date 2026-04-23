package com.backendmorphism.backend.ai.provider;

import com.backendmorphism.backend.ai.dto.AiRequest;
import com.backendmorphism.backend.ai.dto.AiResponse;

public interface AiProvider {

    AiResponse generateResponse(
            AiRequest request
    );
}