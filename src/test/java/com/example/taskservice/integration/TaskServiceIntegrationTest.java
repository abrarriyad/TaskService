package com.example.taskservice.integration;

import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class TaskServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public TaskService taskService() {
            return mock(TaskService.class);
        }
    }

    @Test
    void getTask_ExistingValidUUID_ReturnsTask() throws Exception {
        // Given
        String validUUID = "123e4567-e89b-12d3-a456-426614174000";
        Task task = new Task();
        task.setId(validUUID);
        task.setDescription("Complete project documentation");
        when(taskService.getTask(validUUID)).thenReturn(task);
        
        // When & Then
        mockMvc.perform(get("/getTask/{id}", validUUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.Id").value(validUUID))
                .andExpect(jsonPath("$.description").value("Complete project documentation"));
    }

    @Test
    void getTask_NonExistentUUID_Returns404() throws Exception {
        // Given
        String nonExistentUUID = "00000000-0000-0000-0000-000000000000";
        when(taskService.getTask(nonExistentUUID)).thenThrow(new TaskNotFoundException("Task Not Found"));
        
        // When & Then
        mockMvc.perform(get("/getTask/{id}", nonExistentUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getTask_InvalidUUID_Returns400() throws Exception {
        // Given
        String invalidUUID = "invalid-uuid-format";
        
        // When & Then
        mockMvc.perform(get("/getTask/{id}", invalidUUID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID format"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getTask_EmptyUUID_Returns500() throws Exception {
        // When & Then - Empty UUID path should return 500 (internal server error due to missing path variable)
        mockMvc.perform(get("/getTask/"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getTask_UUIDWithInvalidCharacters_Returns400() throws Exception {
        // Given
        String invalidUUID = "123e4567-e89b-12d3-a456-42661417400g"; // 'g' is invalid hex
        
        // When & Then
        mockMvc.perform(get("/getTask/{id}", invalidUUID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID format"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getTask_UUIDTooShort_Returns400() throws Exception {
        // Given
        String shortUUID = "123e4567-e89b-12d3-a456";
        
        // When & Then
        mockMvc.perform(get("/getTask/{id}", shortUUID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID format"))
                .andExpect(jsonPath("$.status").value(400));
    }
} 