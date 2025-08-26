package com.assignment.playpowerlabs.AIQuizzer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttemptDetails {
    private int score;
    private int correctAnswers;
    private int totalQuestions;
    @JsonFormat(pattern = "yyyy-MM-dd") // Or use "yyyy-MM-dd'T'HH:mm:ss" for LocalDateTime
    private LocalDate attemptedAt;

    private List<AttemptAnswerDto> responses;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttemptAnswerDto {
        private Long questionId;
        private String userResponse;
    }
}
