package com.backendmorphism.backend.interview.dto;

import lombok.Data;

@Data
public class StartInterviewRequest {

    private String role;

    private Integer experienceYears;

    private String type;
}