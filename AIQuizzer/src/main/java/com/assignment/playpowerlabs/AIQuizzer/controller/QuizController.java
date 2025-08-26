package com.assignment.playpowerlabs.AIQuizzer.controller;

import com.assignment.playpowerlabs.AIQuizzer.dto.CreateQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.assignment.playpowerlabs.AIQuizzer.security.JwtUtil;
import com.assignment.playpowerlabs.AIQuizzer.service.AiQuizGeneratorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping({"/api/quizzes"})
public class QuizController {
    @Autowired
    private AiQuizGeneratorService quizService;
    @Autowired
    private JwtUtil jwtUtil;

    public QuizController() {
    }

    @PostMapping("/generate")
    public Quiz generateQuiz(@RequestBody CreateQuizRequest request, HttpServletRequest httpRequest) {
        String token = this.extractJwtFromRequest(httpRequest);
        String username = this.jwtUtil.extractUsername(token);
        return this.quizService.generateQuiz(request, username);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("JWT cookie not found");
    }
}
