package com.backendmorphism.backend.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiEvaluationResponse {

    private Integer score;

    private String feedback;

    private String nextQuestion;
}