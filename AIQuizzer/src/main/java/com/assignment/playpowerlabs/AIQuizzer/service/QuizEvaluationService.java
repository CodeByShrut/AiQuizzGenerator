package com.assignment.playpowerlabs.AIQuizzer.service;

import com.assignment.playpowerlabs.AIQuizzer.dto.QuizEvaluationResult;
import com.assignment.playpowerlabs.AIQuizzer.dto.SubmitQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.model.*;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizAttemptRepository;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizRepository;
import com.assignment.playpowerlabs.AIQuizzer.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.CacheEvict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuizEvaluationService {
    private final QuizRepository quizRepository;
    private final QuizAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AiSuggestionService aiSuggestionService;


    @Transactional
    @CacheEvict(value = "quizAttemptsCache", key = "#request.getQuizId() + ':' + #username")
    public QuizEvaluationResult evaluateQuiz(SubmitQuizRequest request, String username) {
        Quiz quiz = this.quizRepository.findById(Long.parseLong(request.getQuizId()))
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (quiz.getUser() != null) {
            Map<Long, String> correctAnswers = new HashMap<>();
            for (Question q : quiz.getQuestions()) {
                correctAnswers.put(q.getId(), q.getCorrectAnswer().trim().toLowerCase());
            }

            int correct = 0;
            List<AttemptResponse> responses = new ArrayList<>();

            for (SubmitQuizRequest.AnswerSubmission sub : request.getResponses()) {
                Long qid = Long.parseLong(sub.getQuestionId());
                String answer = sub.getUserResponse().trim().toLowerCase();
                if (correctAnswers.containsKey(qid) && correctAnswers.get(qid).equals(answer)) {
                    correct++;
                }

                AttemptResponse r = AttemptResponse.builder()
                        .questionId(qid)
                        .userResponse(sub.getUserResponse())
                        .build();
                responses.add(r);
            }

            int scorePerQ = quiz.getMaxScore() / quiz.getTotalQuestions();
            int totalScore = correct * scorePerQ;

            QuizAttempt attempt = QuizAttempt.builder()
                    .quiz(quiz)
                    .attemptedAt(LocalDate.now())
                    .totalQuestions(quiz.getTotalQuestions())
                    .correctAnswers(correct)
                    .score(totalScore)
                    .user(user)
                    .build();

            for (AttemptResponse r : responses) {
                r.setAttempt(attempt);
            }

//            quiz.setGenerationDate(LocalDate.now());
//            this.quizRepository.save(quiz);
            attempt.setResponses(responses);
            this.attemptRepository.save(attempt);

            String suggestions = aiSuggestionService.generateSuggestions(quiz, responses);

            String formattedSuggestions = suggestions
                    .replaceAll("(?m)^\\s*\\d+\\.\\s", "<br><strong>$0</strong>")  // make numbered points bold
                    .replace("**", "")  // remove markdown-style bold
                    .replaceAll("\n", "<br>");  // ensure newlines become line breaks

            String body = """
                    <html>
                        <body>
                            <h2>Quiz Evaluation Report</h2>
                            <p><strong>Score:</strong> %d / %d</p>
                            <p><strong>Correct Answers:</strong> %d</p>
                            <p><strong>Total Questions:</strong> %d</p>
                            <h3>AI Suggestions for Improvement:</h3>
                            <p>%s</p>
                        </body>
                    </html>
                    """.formatted(totalScore, quiz.getMaxScore(), correct, quiz.getTotalQuestions(), formattedSuggestions);

            String userEmail = quiz.getUser().getEmail();
            emailService.sendEmail(userEmail, "Your Quiz Results & Suggestions", body);

            return new QuizEvaluationResult(quiz.getTotalQuestions(), correct, totalScore);
        } else {
            throw new RuntimeException("You are not authorized to evaluate this quiz.");
        }
    }

}
