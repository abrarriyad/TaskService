package com.example.taskservice.repository;

import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.MalformedJsonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {

    @TempDir
    Path tempDir;

    private TaskRepository taskRepository;
    private ObjectMapper objectMapper;
    private final String testUUID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        taskRepository = new TaskRepository(tempDir.toString(), objectMapper);
    }

    @Test
    void findByUUID_WhenTaskFileExists_ReturnsTask() throws IOException {
        // Given
        String taskJson = """
                {
                  "Id": "%s",
                  "description": "Test task description"
                }
                """.formatted(testUUID);
        
        Path taskFile = tempDir.resolve(testUUID + ".json");
        Files.writeString(taskFile, taskJson);

        // When
        Optional<Task> result = taskRepository.findByUUID(testUUID);

        // Then
        assertTrue(result.isPresent());
        Task task = result.get();
        assertEquals(testUUID, task.getId());
        assertEquals("Test task description", task.getDescription());
    }

    @Test
    void findByUUID_WhenTaskFileNotExists_ReturnsEmpty() {
        // Given
        String nonExistentUUID = "non-existent-uuid";

        // When
        Optional<Task> result = taskRepository.findByUUID(nonExistentUUID);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByUUID_WhenJsonIsMalformed_ThrowsMalformedJsonException() throws IOException {
        // Given
        String malformedJson = "{ invalid json content }";
        Path taskFile = tempDir.resolve(testUUID + ".json");
        Files.writeString(taskFile, malformedJson);

        // When & Then
        MalformedJsonException exception = assertThrows(
                MalformedJsonException.class,
                () -> taskRepository.findByUUID(testUUID)
        );
        
        assertTrue(exception.getMessage().contains("Malformed JSON in task file"));
    }

    @Test
    void findByUUID_WhenFileIsEmpty_ThrowsMalformedJsonException() throws IOException {
        // Given
        Path taskFile = tempDir.resolve(testUUID + ".json");
        Files.writeString(taskFile, "");

        // When & Then
        MalformedJsonException exception = assertThrows(
                MalformedJsonException.class,
                () -> taskRepository.findByUUID(testUUID)
        );
        
        assertTrue(exception.getMessage().contains("Malformed JSON in task file"));
    }

    @Test
    void findByUUID_WhenFileContainsValidJsonButWrongStructure_ThrowsMalformedJsonException() throws IOException {
        // Given
        String wrongStructureJson = """
                {
                  "wrongField": "value",
                  "anotherField": "value"
                }
                """;
        Path taskFile = tempDir.resolve(testUUID + ".json");
        Files.writeString(taskFile, wrongStructureJson);

        // When & Then
        assertThrows(
                MalformedJsonException.class,
                () -> taskRepository.findByUUID(testUUID)
        );
    }
} 