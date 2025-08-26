package com.assignment.playpowerlabs.AIQuizzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetryQuizResponse implements Serializable {
    private String quizId;
    private int totalQuestions;
    private List<QuestionDto> questions;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class QuestionDto implements Serializable {
        private Long questionId;
        private String questionText;
        private List<String> options;
        private String lightHint;
        private String strongHint;
    }
}
