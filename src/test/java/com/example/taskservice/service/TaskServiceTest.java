package com.example.taskservice.service;

import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private final String testUUID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(testUUID);
        testTask.setDescription("Test task description");
    }

    @Test
    void getTask_WhenTaskExists_ReturnsTask() {
        // Given
        when(cacheService.getTaskFromCache(testUUID)).thenReturn(testTask);

        // When
        Task result = taskService.getTask(testUUID);

        // Then
        assertNotNull(result);
        assertEquals(testUUID, result.getId());
        assertEquals("Test task description", result.getDescription());
        verify(cacheService, times(1)).getTaskFromCache(testUUID);
    }

    @Test
    void getTask_WhenTaskNotFound_ThrowsTaskNotFoundException() {
        // Given
        when(cacheService.getTaskFromCache(testUUID)).thenReturn(null);

        // When & Then
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class, 
                () -> taskService.getTask(testUUID)
        );
        
        assertEquals("Task Not Found", exception.getMessage());
        verify(cacheService, times(1)).getTaskFromCache(testUUID);
    }

    @Test
    void getTask_WithNullId_ThrowsTaskNotFoundException() {
        // Given
        when(cacheService.getTaskFromCache(null)).thenReturn(null);

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(null));
        verify(cacheService, times(1)).getTaskFromCache(null);
    }

    @Test
    void getTask_WithEmptyId_ThrowsTaskNotFoundException() {
        // Given
        String emptyId = "";
        when(cacheService.getTaskFromCache(emptyId)).thenReturn(null);

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(emptyId));
        verify(cacheService, times(1)).getTaskFromCache(emptyId);
    }
} 