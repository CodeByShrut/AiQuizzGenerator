package com.assignment.playpowerlabs.AIQuizzer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final String message;
    private final String errorCode;
    private final String path;

    public ErrorResponse(String message, String errorCode, String path) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
    }
}
