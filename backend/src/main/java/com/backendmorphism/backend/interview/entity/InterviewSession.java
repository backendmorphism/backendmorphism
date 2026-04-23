package com.backendmorphism.backend.interview.entity;

import com.backendmorphism.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_sessions")
@Data
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Java Developer, Frontend, etc.
    private String role;

    // 1, 2, 3, 5 years etc.
    private Integer experienceYears;

    // TEXT / VOICE / VIDEO
    private String interviewMode;

    // FREE / PRO
    private String type;

    // STARTED / COMPLETED / CANCELLED
    private String status;

    private Integer overallScore;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {

        this.startedAt = LocalDateTime.now();

        this.status = "STARTED";

        this.overallScore = 0;

        // default MVP mode
        if (this.interviewMode == null) {
            this.interviewMode = "TEXT";
        }

        // default free tier
        if (this.type == null) {
            this.type = "FREE";
        }
    }
}