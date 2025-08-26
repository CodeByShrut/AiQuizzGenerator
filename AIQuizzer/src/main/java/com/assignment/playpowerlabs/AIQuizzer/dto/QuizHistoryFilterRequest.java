package com.assignment.playpowerlabs.AIQuizzer.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class QuizHistoryFilterRequest {
    private Integer grade;
    private String subject;
    private Integer minScore;
    private Integer maxScore;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate generationDate;
}
