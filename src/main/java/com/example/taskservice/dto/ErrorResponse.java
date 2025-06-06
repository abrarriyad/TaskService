package com.example.taskservice.dto;

public record ErrorResponse(
        String message,
        int status
) {
} 