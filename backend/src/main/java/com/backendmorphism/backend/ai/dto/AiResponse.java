package com.backendmorphism.backend.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiResponse {

    private String content;
}