package com.backendmorphism.backend.interview.dto;

import com.backendmorphism.backend.interview.entity.InterviewInteraction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InterviewStartResponse {

    private Long sessionId;

    private String role;

    private Integer experienceYears;

    private String type;

    private String status;

    private Integer overallScore;

    private List<InterviewInteraction> interactions;
}