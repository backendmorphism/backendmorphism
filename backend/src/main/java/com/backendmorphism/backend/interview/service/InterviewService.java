package com.backendmorphism.backend.interview.service;

import com.backendmorphism.backend.ai.dto.AiEvaluationResponse;
import com.backendmorphism.backend.ai.service.AiInterviewService;
import com.backendmorphism.backend.ai.service.AiService;
import com.backendmorphism.backend.ai.service.AiSummaryService;
import com.backendmorphism.backend.interview.dto.InterviewStartResponse;
import com.backendmorphism.backend.interview.dto.StartInterviewRequest;
import com.backendmorphism.backend.interview.dto.SubmitAnswerRequest;
import com.backendmorphism.backend.interview.entity.InterviewInteraction;
import com.backendmorphism.backend.interview.entity.InterviewSession;
import com.backendmorphism.backend.interview.entity.InterviewSummary;
import com.backendmorphism.backend.interview.repository.InterviewInteractionRepository;
import com.backendmorphism.backend.interview.repository.InterviewSessionRepository;
import com.backendmorphism.backend.interview.repository.InterviewSummaryRepository;
import com.backendmorphism.backend.user.entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewService {

    @Autowired
    private InterviewSessionRepository interviewSessionRepository;

    @Autowired
    private InterviewInteractionRepository interactionRepository;

    @Autowired
    private AiService aiService;

    @Autowired
    private AiInterviewService aiInterviewService;

    @Autowired
    private AiSummaryService aiSummaryService;

    @Autowired
    private InterviewSummaryRepository summaryRepository;

    /*
     * START INTERVIEW
     */
    public InterviewStartResponse startInterview(
            StartInterviewRequest request,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        InterviewSession session = new InterviewSession();

        session.setRole(request.getRole());

        session.setExperienceYears(
                request.getExperienceYears()
        );

        session.setType(request.getType());

        session.setUser(user);

        InterviewSession savedSession =
                interviewSessionRepository.save(session);

        // AI GENERATED FIRST QUESTION
        String firstQuestion =
                aiService.generateResponse(
                        "You are a senior technical interviewer.",
                        """
                        Generate ONE realistic technical interview question.

                        Role: %s
                        Experience Level: %d years

                        Keep it concise and practical.
                        """
                                .formatted(
                                        savedSession.getRole(),
                                        savedSession.getExperienceYears()
                                )
                );

        createQuestion(
                savedSession,
                1,
                firstQuestion
        );

        return InterviewStartResponse.builder()
                .sessionId(savedSession.getId())
                .role(savedSession.getRole())
                .experienceYears(savedSession.getExperienceYears())
                .type(savedSession.getType())
                .status(savedSession.getStatus())
                .overallScore(savedSession.getOverallScore())
                .interactions(
                        interactionRepository
                                .findByInterviewSessionIdOrderBySequenceNumberAsc(
                                        savedSession.getId()
                                )
                )
                .build();
    }

    /*
     * SUBMIT ANSWER
     */
    public void submitAnswer(
            Long sessionId,
            SubmitAnswerRequest request,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        InterviewSession session =
                interviewSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview session not found"
                                ));

        // ownership validation
        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        InterviewInteraction questionInteraction =
                interactionRepository
                        .findByInterviewSessionIdAndSequenceNumberAndInteractionType(
                                sessionId,
                                request.getSequenceNumber(),
                                "QUESTION"
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Question not found"
                                ));

        /*
         * SAVE USER ANSWER
         */
        InterviewInteraction answerInteraction =
                new InterviewInteraction();

        answerInteraction.setInteractionType("ANSWER");

        answerInteraction.setUserMessage(
                request.getAnswer()
        );

        answerInteraction.setSequenceNumber(
                request.getSequenceNumber()
        );

        answerInteraction.setInterviewSession(session);

        interactionRepository.save(answerInteraction);

        /*
         * AI EVALUATION
         */
        List<InterviewInteraction> interactions =
                interactionRepository
                        .findByInterviewSessionIdOrderBySequenceNumberAsc(
                                sessionId
                        );

        String conversationTranscript =
                buildConversationTranscript(interactions);

        AiEvaluationResponse evaluation =
                aiInterviewService.evaluateAnswer(
                        session,
                        conversationTranscript,
                        request.getAnswer()
                );

        /*
         * SAVE FEEDBACK
         */
        InterviewInteraction feedbackInteraction =
                new InterviewInteraction();

        feedbackInteraction.setInteractionType("FEEDBACK");

        feedbackInteraction.setAiFeedback(
                evaluation.getFeedback()
        );

        feedbackInteraction.setScore(
                evaluation.getScore()
        );

        feedbackInteraction.setSequenceNumber(
                request.getSequenceNumber()
        );

        feedbackInteraction.setInterviewSession(session);

        interactionRepository.save(feedbackInteraction);

        /*
         * UPDATE OVERALL SCORE
         */
        Integer currentScore =
                session.getOverallScore() == null
                        ? 0
                        : session.getOverallScore();

        session.setOverallScore(
                currentScore + evaluation.getScore()
        );

        /*
         * COMPLETE INTERVIEW AFTER 5 QUESTIONS
         */
        if (request.getSequenceNumber() >= 5) {

            session.setStatus("COMPLETED");

            session.setCompletedAt(
                    LocalDateTime.now()
            );

            interviewSessionRepository.save(session);

            List<InterviewInteraction> interaction =
                    interactionRepository
                            .findByInterviewSessionIdOrderBySequenceNumberAsc(
                                    session.getId()
                            );

            JsonNode summaryJson =
                    aiSummaryService.generateSummary(
                            session,
                            interaction
                    );

            InterviewSummary summary =
                    new InterviewSummary();

            summary.setOverallScore(
                    summaryJson.get("overallScore").asInt()
            );

            summary.setTechnicalScore(
                    summaryJson.get("technicalScore").asInt()
            );

            summary.setCommunicationScore(
                    summaryJson.get("communicationScore").asInt()
            );

            summary.setHiringRecommendation(
                    summaryJson.get("hiringRecommendation").asText()
            );

            summary.setStrengths(
                    summaryJson.get("strengths").asText()
            );

            summary.setWeaknesses(
                    summaryJson.get("weaknesses").asText()
            );

            summary.setImprovementAreas(
                    summaryJson.get("improvementAreas").asText()
            );

            summary.setOverallFeedback(
                    summaryJson.get("overallFeedback").asText()
            );

            summary.setInterviewSession(session);

            summaryRepository.save(summary);

            return;
        }

        /*
         * GENERATE NEXT QUESTION
         */
        Integer nextSequence =
                request.getSequenceNumber() + 1;

        createQuestion(
                session,
                nextSequence,
                evaluation.getNextQuestion()
        );

        interviewSessionRepository.save(session);
    }

    /*
     * GET INTERVIEW TIMELINE
     */
    public List<InterviewInteraction> getInteractions(
            Long sessionId,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        InterviewSession session =
                interviewSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview session not found"
                                ));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        return interactionRepository
                .findByInterviewSessionIdOrderBySequenceNumberAsc(
                        sessionId
                );
    }

    /*
     * CREATE QUESTION INTERACTION
     */
    private void createQuestion(
            InterviewSession session,
            Integer sequence,
            String questionText
    ) {

        InterviewInteraction interaction =
                new InterviewInteraction();

        interaction.setInteractionType("QUESTION");

        interaction.setAiMessage(questionText);

        interaction.setSequenceNumber(sequence);

        interaction.setInterviewSession(session);

        interactionRepository.save(interaction);
    }

    private String buildConversationTranscript(
            List<InterviewInteraction> interactions
    ) {

        StringBuilder transcript =
                new StringBuilder();

        // only last 8 interactions
        int start =
                Math.max(0, interactions.size() - 8);

        for (int i = start; i < interactions.size(); i++) {

            InterviewInteraction interaction =
                    interactions.get(i);

            if (interaction.getAiMessage() != null) {

                transcript.append("AI: ")
                        .append(interaction.getAiMessage())
                        .append("\n");
            }

            if (interaction.getUserMessage() != null) {

                transcript.append("USER: ")
                        .append(interaction.getUserMessage())
                        .append("\n");
            }

            if (interaction.getAiFeedback() != null) {

                transcript.append("AI_FEEDBACK: ")
                        .append(interaction.getAiFeedback())
                        .append("\n");
            }
        }

        return transcript.toString();
    }
}