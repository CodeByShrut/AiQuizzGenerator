package com.assignment.playpowerlabs.AIQuizzer.dto;

import lombok.Getter;

@Getter
public class QuizEvaluationResult {
    private int totalQuestions;
    private int correctAnswers;
    private int score;

    public QuizEvaluationResult(int totalQuestions, int correctAnswers, int score) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
    }

}
