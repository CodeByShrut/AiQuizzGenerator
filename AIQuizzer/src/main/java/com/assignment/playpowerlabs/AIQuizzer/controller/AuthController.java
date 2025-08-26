package com.assignment.playpowerlabs.AIQuizzer.controller;

import com.assignment.playpowerlabs.AIQuizzer.dto.AuthRequest;
import com.assignment.playpowerlabs.AIQuizzer.dto.AuthResponse;
import com.assignment.playpowerlabs.AIQuizzer.model.User;
import com.assignment.playpowerlabs.AIQuizzer.model.Role;
import com.assignment.playpowerlabs.AIQuizzer.repository.UserRepository;
import com.assignment.playpowerlabs.AIQuizzer.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request, HttpServletResponse response) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists!";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Role role = request.getRole(); // Already an enum
        User user = new User(request.getUsername(), hashedPassword, request.getEmail(), role);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        setJwtCookie(response, token);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request, HttpServletResponse response) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        setJwtCookie(response, token);
        return new AuthResponse(token);
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 1 day
        response.addCookie(cookie);
    }
}
