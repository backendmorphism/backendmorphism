package com.backendmorphism.backend.interview.dto;

import lombok.Data;

@Data
public class SubmitAnswerRequest {

    private Integer sequenceNumber;

    private String answer;
}