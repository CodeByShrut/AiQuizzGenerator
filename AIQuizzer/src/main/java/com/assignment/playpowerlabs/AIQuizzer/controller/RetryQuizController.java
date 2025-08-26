package com.assignment.playpowerlabs.AIQuizzer.controller;

import com.assignment.playpowerlabs.AIQuizzer.dto.QuizAttemptDetails;
import com.assignment.playpowerlabs.AIQuizzer.dto.RetryQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.dto.RetryQuizResponse;
import com.assignment.playpowerlabs.AIQuizzer.security.JwtUtil;
import com.assignment.playpowerlabs.AIQuizzer.service.RetryQuizService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/quiz"})
public class RetryQuizController {
    private final RetryQuizService retryQuizService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping({"/{quizId}/retry"})
    public RetryQuizResponse retry(@PathVariable String quizId, HttpServletRequest httpRequest) {
        String token = this.extractJwtFromCookie(httpRequest);
//        if (!jwtUtil.isTokenValid(token)) {
//            throw new RuntimeException("Token is expired or invalid");
//        }
        String username = this.jwtUtil.extractUsername(token);
        return this.retryQuizService.retryQuiz(quizId, username);
    }

    @GetMapping({"/{quizId}/attempts"})
    public List<QuizAttemptDetails> getAttempts(@PathVariable Long quizId, HttpServletRequest httpRequest) {
        String token = this.extractJwtFromCookie(httpRequest);
//        if (!jwtUtil.isTokenValid(token)) {
//            throw new RuntimeException("Token is expired or invalid");
//        }
        String username = this.jwtUtil.extractUsername(token);
        return this.retryQuizService.getQuizAttempts(quizId, username);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("Missing JWT token in cookies");
    }

    public RetryQuizController(final RetryQuizService retryQuizService) {
        this.retryQuizService = retryQuizService;
    }
}
