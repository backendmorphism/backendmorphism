package com.backendmorphism.backend.interview.repository;

import com.backendmorphism.backend.interview.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSessionRepository
        extends JpaRepository<InterviewSession, Long> {
}