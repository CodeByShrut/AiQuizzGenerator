package com.assignment.playpowerlabs.AIQuizzer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate attemptedAt;
    private int totalQuestions;
    private int correctAnswers;
    private int score;

    @ManyToOne
    private Quiz quiz;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL)
    private List<AttemptResponse> responses;
}
