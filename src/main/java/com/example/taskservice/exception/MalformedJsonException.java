package com.example.taskservice.exception;

public class MalformedJsonException extends RuntimeException {
    public MalformedJsonException(String message, Throwable cause) {
        super(message, cause);
    }
} 