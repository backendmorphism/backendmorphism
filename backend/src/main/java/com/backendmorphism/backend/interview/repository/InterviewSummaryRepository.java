package com.backendmorphism.backend.interview.repository;

import com.backendmorphism.backend.interview.entity.InterviewSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewSummaryRepository
        extends JpaRepository<InterviewSummary, Long> {

    Optional<InterviewSummary>
    findByInterviewSessionId(Long sessionId);
}