package com.saas.spring.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    String message,
    String details,
    int status,
    LocalDateTime timestamp,
    String path
) {
    public ErrorResponse(String message, String details, int status, String path) {
        this(message, details, status, LocalDateTime.now(), path);
    }
}