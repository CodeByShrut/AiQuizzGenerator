package com.assignment.playpowerlabs.AIQuizzer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubmitQuizRequest {
    private String quizId;
    private List<AnswerSubmission> responses;

    @Setter
    @Getter
    public static class AnswerSubmission {
        private String questionId;
        private String userResponse;
    }
}
