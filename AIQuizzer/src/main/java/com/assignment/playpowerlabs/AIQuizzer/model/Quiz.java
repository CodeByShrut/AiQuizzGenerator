package com.assignment.playpowerlabs.AIQuizzer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private int grade;
    @Setter
    private String subject;
    @Setter
    private int totalQuestions;
    @Setter
    private int maxScore;
    @Setter
    private String difficulty;
    @Setter
    @Column(name = "generation_date")
    private LocalDate generationDate;

    @Setter
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();


    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addQuestion(Question question) {
        question.setQuiz(this);
    }

}
