package com.backendmorphism.backend.ai.prompt;

import org.springframework.stereotype.Service;

@Service
public class InterviewPromptService {

    public String buildQuestionPrompt(
            String role,
            Integer experienceYears
    ) {

        return """
                Generate a realistic technical interview question.

                Role: %s
                Experience: %d years

                Keep question concise and realistic.
                """
                .formatted(role, experienceYears);
    }

    public String buildEvaluationPrompt(
            String question,
            String answer
    ) {

        return """
                Evaluate this interview answer.

                Question:
                %s

                Answer:
                %s

                Provide:
                - score out of 10
                - strengths
                - weaknesses
                - improvement suggestion
                """
                .formatted(question, answer);
    }

    public String buildInterviewEvaluationPrompt(
            String role,
            Integer experienceYears,
            String conversationHistory,
            String currentAnswer
    ) {

        return """
            You are a senior technical interviewer.

            Role: %s
            Experience Level: %d years

            Conversation History:
            %s

            Candidate's Latest Answer:
            %s

            Evaluate the latest answer in context of the interview.

            Return STRICT JSON only:

            {
              "score": 1-10,
              "feedback": "short feedback",
              "nextQuestion": "next interview question"
            }

            Rules:
            - Ask contextual follow-up questions when relevant
            - Avoid repeating previous questions
            - Increase difficulty gradually
            - Keep feedback concise and realistic
            """
                .formatted(
                        role,
                        experienceYears,
                        conversationHistory,
                        currentAnswer
                );
    }

    public String buildInterviewSummaryPrompt(
            String role,
            Integer experienceYears,
            String interviewTranscript
    ) {

        return """
            You are an expert technical interviewer.

            Role: %s
            Experience Level: %d years

            Interview Transcript:
            %s

            Generate a final interview evaluation.

            Return STRICT JSON only:

            {
              "overallScore": 1-10,
              "technicalScore": 1-10,
              "communicationScore": 1-10,
              "hiringRecommendation": "Strong Hire / Hire / No Hire",
              "strengths": "...",
              "weaknesses": "...",
              "improvementAreas": "...",
              "overallFeedback": "..."
            }

            Keep feedback concise but insightful.
            """
                .formatted(
                        role,
                        experienceYears,
                        interviewTranscript
                );
    }
}