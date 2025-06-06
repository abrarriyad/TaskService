package com.example.taskservice.exception;

import com.example.taskservice.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private TaskNotFoundException taskNotFoundException;
    private MalformedJsonException malformedJsonException;
    private RuntimeException genericException;

    @BeforeEach
    void setUp() {
        taskNotFoundException = new TaskNotFoundException("Test task not found");
        malformedJsonException = new MalformedJsonException("Test malformed JSON", new RuntimeException("JSON parse error"));
        genericException = new RuntimeException("Test generic error");
    }

    @Test
    void handleTaskNotFoundException_ReturnsNotFound() {
        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleTaskNotFoundException(taskNotFoundException);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Task not found", response.getBody().message());
        assertEquals(404, response.getBody().status());
    }

    @Test
    void handleMalformedJsonException_ReturnsInternalServerError() {
        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleMalformedJsonException(malformedJsonException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("Task data corrupted"));
        assertTrue(response.getBody().message().contains("Test malformed JSON"));
        assertEquals(500, response.getBody().status());
    }

    @Test
    void handleGenericException_ReturnsInternalServerError() {
        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleGenericException(genericException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().message());
        assertEquals(500, response.getBody().status());
    }

    @Test
    void taskNotFoundException_WithNullMessage_HandlesGracefully() {
        // Given
        TaskNotFoundException nullMessageException = new TaskNotFoundException(null);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleTaskNotFoundException(nullMessageException);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody().message());
    }

    @Test
    void malformedJsonException_WithNullMessage_HandlesGracefully() {
        // Given
        MalformedJsonException nullMessageException = new MalformedJsonException(null, new RuntimeException("cause"));

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler
                .handleMalformedJsonException(nullMessageException);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().message().contains("Task data corrupted"));
    }
} 