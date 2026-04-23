package com.backendmorphism.backend.interview.controller;

import com.backendmorphism.backend.common.dto.ApiResponse;
import com.backendmorphism.backend.interview.dto.*;
import com.backendmorphism.backend.interview.entity.InterviewInteraction;
import com.backendmorphism.backend.interview.entity.InterviewSummary;
import com.backendmorphism.backend.interview.service.InterviewService;
import com.backendmorphism.backend.interview.service.InterviewSummaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private InterviewSummaryService summaryService;

    @PostMapping("/start")
    public ApiResponse<InterviewStartResponse> startInterview(
            @Valid @RequestBody StartInterviewRequest request,
            Authentication authentication
    ) {
        InterviewStartResponse response =
                interviewService.startInterview(
                        request,
                        authentication
                );
        return ApiResponse
                .<InterviewStartResponse>builder()
                .success(true)
                .message("Interview started successfully")
                .data(response)
                .build();
    }

    @PostMapping("/{sessionId}/answer")
    public ApiResponse<Void> submitAnswer(
            @PathVariable Long sessionId,
            @RequestBody SubmitAnswerRequest request,
            Authentication authentication
    ) {
        interviewService.submitAnswer(
                sessionId,
                request,
                authentication
        );

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Answer submitted successfully")
                .build();
    }

    @GetMapping("/{sessionId}/interactions")
    public ApiResponse<List<InterviewInteraction>> getInteractions(
            @PathVariable Long sessionId,
            Authentication authentication
    ) {
        List<InterviewInteraction> interactions =
                interviewService.getInteractions(
                        sessionId,
                        authentication
                );

        return ApiResponse
                .<List<InterviewInteraction>>builder()
                .success(true)
                .message("Interactions fetched successfully")
                .data(interactions)
                .build();
    }

    @GetMapping("/{sessionId}/summary")
    public ApiResponse<InterviewSummaryResponse> getSummary(
            @PathVariable Long sessionId
    ) {
        InterviewSummaryResponse response =
                summaryService.getSummary(sessionId);

        return ApiResponse
                .<InterviewSummaryResponse>builder()
                .success(true)
                .message("Interview summary fetched successfully")
                .data(response)
                .build();
    }
}