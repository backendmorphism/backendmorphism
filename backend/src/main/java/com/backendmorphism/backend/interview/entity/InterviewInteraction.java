package com.backendmorphism.backend.interview.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_interactions")
@Data
public class InterviewInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        QUESTION
        ANSWER
        FOLLOW_UP
        FEEDBACK
    */
    private String interactionType;

    /*
        TEXT
        AUDIO
        VIDEO
    */
    private String contentType;

    @Column(columnDefinition = "TEXT")
    private String aiMessage;

    @Column(columnDefinition = "TEXT")
    private String userMessage;

    /*
        speech-to-text transcript
    */
    @Column(columnDefinition = "TEXT")
    private String transcript;

    /*
        future audio storage URL
    */
    private String audioUrl;

    /*
        future webcam recording URL
    */
    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private Integer score;

    private Integer sequenceNumber;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "interview_session_id")
    private InterviewSession interviewSession;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();

        if (this.score == null) {
            this.score = 0;
        }

        if (this.contentType == null) {
            this.contentType = "TEXT";
        }
    }
}