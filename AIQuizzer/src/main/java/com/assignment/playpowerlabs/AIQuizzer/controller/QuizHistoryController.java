package com.assignment.playpowerlabs.AIQuizzer.controller;

import com.assignment.playpowerlabs.AIQuizzer.dto.QuizHistoryFilterRequest;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.assignment.playpowerlabs.AIQuizzer.security.JwtUtil;
import com.assignment.playpowerlabs.AIQuizzer.service.QuizHistoryService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/quiz-history"})
public class QuizHistoryController {
    private final QuizHistoryService quizHistoryService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping({"/filter"})
    public List<Quiz> getFilteredHistory(@RequestBody QuizHistoryFilterRequest request, HttpServletRequest httpRequest) {
        String token = this.extractJwtFromCookie(httpRequest);
//        if (!jwtUtil.isTokenValid(token)) {
//            throw new RuntimeException("Token is expired or invalid");
//        }
        String username = this.jwtUtil.extractUsername(token);
        return this.quizHistoryService.getFilteredQuizHistory(request, username);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("Missing JWT token in cookies");
    }

    public QuizHistoryController(final QuizHistoryService quizHistoryService) {
        this.quizHistoryService = quizHistoryService;
    }
}
