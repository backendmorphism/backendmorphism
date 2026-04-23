package com.backendmorphism.backend.interview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewSummaryResponse {

    private Integer overallScore;

    private Integer technicalScore;

    private Integer communicationScore;

    private String hiringRecommendation;

    private String strengths;

    private String weaknesses;

    private String improvementAreas;

    private String overallFeedback;
}