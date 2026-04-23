package com.backendmorphism.backend.interview.service;

import com.backendmorphism.backend.interview.dto.InterviewSummaryResponse;
import com.backendmorphism.backend.interview.entity.InterviewSummary;
import com.backendmorphism.backend.interview.repository.InterviewSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewSummaryService {

    @Autowired
    private InterviewSummaryRepository summaryRepository;

    public InterviewSummaryResponse getSummary(
            Long sessionId
    ) {

        InterviewSummary summary =
                summaryRepository
                        .findByInterviewSessionId(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Summary not found"
                                ));

        return InterviewSummaryResponse.builder()
                .overallScore(summary.getOverallScore())
                .technicalScore(summary.getTechnicalScore())
                .communicationScore(
                        summary.getCommunicationScore()
                )
                .hiringRecommendation(
                        summary.getHiringRecommendation()
                )
                .strengths(summary.getStrengths())
                .weaknesses(summary.getWeaknesses())
                .improvementAreas(
                        summary.getImprovementAreas()
                )
                .overallFeedback(
                        summary.getOverallFeedback()
                )
                .build();
    }
}