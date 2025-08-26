package com.assignment.playpowerlabs.AIQuizzer.repository;

import com.assignment.playpowerlabs.AIQuizzer.model.QuizAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByQuizId(Long quizId);
    List<QuizAttempt> findByQuizIdAndUserId(Long quizId, Long userId);
}
