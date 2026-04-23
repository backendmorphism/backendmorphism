package com.backendmorphism.backend.interview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewSessionResponse {

    private Long sessionId;

    private String role;

    private Integer experienceYears;

    private String type;

    private String status;

    private Integer overallScore;
}