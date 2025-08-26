package com.assignment.playpowerlabs.AIQuizzer.dto;

import com.assignment.playpowerlabs.AIQuizzer.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private Role role; // Use enum here (TEACHER or STUDENT)
}
