package com.backendmorphism.backend.interview.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_summaries")
@Data
public class InterviewSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer overallScore;

    private Integer technicalScore;

    private Integer communicationScore;

    private String hiringRecommendation;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String weaknesses;

    @Column(columnDefinition = "TEXT")
    private String improvementAreas;

    @Column(columnDefinition = "TEXT")
    private String overallFeedback;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "interview_session_id")
    private InterviewSession interviewSession;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}