package com.assignment.playpowerlabs.AIQuizzer.repository;

import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {
}
