package com.assignment.playpowerlabs.AIQuizzer.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateQuizRequest {
    private int grade;
    private String subject;
    private int totalQuestions;
    private int maxScore;
    private String difficulty;

    public CreateQuizRequest() {
    }

}
