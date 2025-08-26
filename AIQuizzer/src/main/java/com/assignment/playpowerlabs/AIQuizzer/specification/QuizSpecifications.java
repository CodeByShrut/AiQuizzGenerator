package com.assignment.playpowerlabs.AIQuizzer.specification;

import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class QuizSpecifications {
    public QuizSpecifications() {
    }

    public static Specification<Quiz> belongsToUser(String username) {
        return (root, query, cb) -> cb.equal(root.get("user").get("username"), username);
    }

    public static Specification<Quiz> hasGrade(Integer grade) {
        return (root, query, cb) -> grade == null ? null : cb.equal(root.get("grade"), grade);
    }

    public static Specification<Quiz> hasSubject(String subject) {
        return (root, query, cb) -> subject == null ? null : cb.equal(root.get("subject"), subject);
    }

    public static Specification<Quiz> minScore(Integer minScore) {
        return (root, query, cb) -> minScore == null ? null : cb.ge(root.get("maxScore"), minScore);
    }

    public static Specification<Quiz> maxScore(Integer maxScore) {
        return (root, query, cb) -> maxScore == null ? null : cb.le(root.get("maxScore"), maxScore);
    }

    public static Specification<Quiz> betweenDates(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("generationDate"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("generationDate"), from);
            } else {
                return to != null ? cb.lessThanOrEqualTo(root.get("generationDate"), to) : null;
            }
        };
    }
    public static Specification<Quiz> onDate(LocalDate genDate) {
        return (root, query, cb) -> {
            if (genDate != null) {
                return cb.between(root.get("generationDate"), genDate, genDate);
            }
            else {
                return null;
            }
        };
    }
}
