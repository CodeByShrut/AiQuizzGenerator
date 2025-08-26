package com.assignment.playpowerlabs.AIQuizzer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    private String correctAnswer;

    private String lightHint;
    private String strongHint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    @JsonIgnoreProperties("questions")
    private Quiz quiz;
}
