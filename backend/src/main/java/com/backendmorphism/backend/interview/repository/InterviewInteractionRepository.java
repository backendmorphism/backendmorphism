package com.backendmorphism.backend.interview.repository;

import com.backendmorphism.backend.interview.entity.InterviewInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InterviewInteractionRepository
        extends JpaRepository<InterviewInteraction, Long> {
    List<InterviewInteraction>
    findByInterviewSessionIdOrderBySequenceNumberAsc(
            Long sessionId
    );

    Optional<InterviewInteraction>
    findByInterviewSessionIdAndSequenceNumberAndInteractionType(
            Long sessionId,
            Integer sequenceNumber,
            String interactionType
    );
}