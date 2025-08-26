package com.assignment.playpowerlabs.AIQuizzer.controller;
import com.assignment.playpowerlabs.AIQuizzer.dto.QuizEvaluationResult;
import com.assignment.playpowerlabs.AIQuizzer.dto.SubmitQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.security.JwtUtil;
import com.assignment.playpowerlabs.AIQuizzer.service.QuizEvaluationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/quizzes"})
public class QuizEvaluationController {
    @Autowired
    private QuizEvaluationService evaluationService;
    @Autowired
    private JwtUtil jwtUtil;

    public QuizEvaluationController() {
    }

    @PostMapping({"/submit"})
    public QuizEvaluationResult submitQuiz(@RequestBody SubmitQuizRequest request, HttpServletRequest httpRequest) {
        String token = this.extractJwtFromCookie(httpRequest);
        String username = this.jwtUtil.extractUsername(token);
        return this.evaluationService.evaluateQuiz(request, username);
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
}
