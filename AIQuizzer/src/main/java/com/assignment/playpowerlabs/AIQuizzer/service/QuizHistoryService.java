package com.assignment.playpowerlabs.AIQuizzer.service;

import com.assignment.playpowerlabs.AIQuizzer.dto.QuizHistoryFilterRequest;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizRepository;
import com.assignment.playpowerlabs.AIQuizzer.specification.QuizSpecifications;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizHistoryService {
    private final QuizRepository quizRepository;

//    @Cacheable(value = "quizHistory", key = "#username + ':' + #request.hashCode()")
    public List<Quiz> getFilteredQuizHistory(QuizHistoryFilterRequest request, String username) {
//        Specification<Quiz> spec = Specification.where(null);
        Specification<Quiz> spec = (root, query, cb) -> cb.conjunction();
        if (request.getGrade() != null) {
            spec = spec.and(QuizSpecifications.hasGrade(request.getGrade()));
        }

        if (request.getSubject() != null) {
            spec = spec.and(QuizSpecifications.hasSubject(request.getSubject()));
        }

        if (request.getMinScore() != null) {
            spec = spec.and(QuizSpecifications.minScore(request.getMinScore()));
        }

        if (request.getMaxScore() != null) {
            spec = spec.and(QuizSpecifications.maxScore(request.getMaxScore()));
        }

        if (request.getFromDate() != null || request.getToDate() != null) {
            spec = spec.and(QuizSpecifications.betweenDates(request.getFromDate(), request.getToDate()));
        }
        if (request.getGenerationDate() != null) {
            spec = spec.and(QuizSpecifications.onDate(request.getGenerationDate()));
        }

        return this.quizRepository.findAll(spec);
    }
}
