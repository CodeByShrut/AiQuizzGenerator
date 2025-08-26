package com.assignment.playpowerlabs.AIQuizzer.service;

import com.assignment.playpowerlabs.AIQuizzer.dto.QuizAttemptDetails;
import com.assignment.playpowerlabs.AIQuizzer.dto.RetryQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.dto.RetryQuizResponse;
import com.assignment.playpowerlabs.AIQuizzer.dto.QuizAttemptDetails.AttemptAnswerDto;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.assignment.playpowerlabs.AIQuizzer.model.QuizAttempt;
import com.assignment.playpowerlabs.AIQuizzer.model.User;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizAttemptRepository;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizRepository;
import com.assignment.playpowerlabs.AIQuizzer.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RetryQuizService {
    private final QuizRepository quizRepository;
    private final QuizAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    @Transactional
    @Cacheable(value = "quizRetryCache", key = "#quizId")
    public RetryQuizResponse retryQuiz(String quizId, String username) {
        Quiz quiz = quizRepository.findById(Long.parseLong(quizId))
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

//        if (quiz.getUser() == null || !quiz.getUser().getUsername().equals(username)) {
//            throw new RuntimeException("You are not authorized to access this quiz.");
//        }

        List<RetryQuizResponse.QuestionDto> questions = quiz.getQuestions().stream()
                .map(q -> RetryQuizResponse.QuestionDto.builder()
                        .questionId(q.getId())
                        .questionText(q.getQuestionText())
                        .options(q.getOptions() == null ? List.of() : new ArrayList<>(q.getOptions()))
                        .lightHint(q.getLightHint())
                        .strongHint(q.getStrongHint())
                        .build())
                .collect(Collectors.toList());

        return RetryQuizResponse.builder()
                .quizId(quizId)
                .totalQuestions(questions.size())
                .questions(questions)
                .build();
    }

    @Cacheable(value = "quizAttemptsCache", key = "#quizId + ':' + #username")
    public List<QuizAttemptDetails> getQuizAttempts(Long quizId, String username) {
        Quiz quiz = (Quiz)this.quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
//        if (quiz.getUser() != null && quiz.getUser().getUsername().equals(username)) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<QuizAttempt> attempts = this.attemptRepository.findByQuizIdAndUserId(quizId,user.getId());
        return attempts.stream().map((attempt) -> {
            List<QuizAttemptDetails.AttemptAnswerDto> answers = attempt.getResponses().stream().map((r) -> AttemptAnswerDto.builder().questionId(r.getQuestionId()).userResponse(r.getUserResponse()).build()).toList();
            return QuizAttemptDetails.builder().score(attempt.getScore()).correctAnswers(attempt.getCorrectAnswers()).totalQuestions(attempt.getTotalQuestions()).attemptedAt(attempt.getAttemptedAt()).responses(answers).build();
        }).toList();
//        } else {
//            throw new RuntimeException("You are not authorized to get attempts of this quiz.");
//        }
    }

}
