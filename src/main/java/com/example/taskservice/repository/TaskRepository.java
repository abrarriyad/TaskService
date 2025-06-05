package com.example.taskservice.repository;

import com.example.taskservice.entity.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Repository
@Slf4j
public class TaskRepository {

    private final Path path;
    private final ObjectMapper objectMapper;

    public TaskRepository(@Value("${task.file.base-path:Tasks}") String basePath, ObjectMapper objectMapper) {
        this.path = Path.of(basePath);
        this.objectMapper = objectMapper;

        log.info("Task repository initialized with base path: {}", this.path.toAbsolutePath());
    }

    public Optional<Task> findByUUID(String uuid){
        try{
            Path taskFile = path.resolve(uuid+".json");
            if (Files.notExists(taskFile)) {
                log.debug("Task file not found: {}", taskFile);
                return Optional.empty();
            }
            String content = Files.readString(taskFile);
            Task task = objectMapper.readValue(content, Task.class);

            log.debug("Successfully loaded task from file: {}", uuid);
            return Optional.of(task);

        } catch (IOException e){
            log.error("Error reading task file: {}", uuid, e);
            throw new RuntimeException("Failed to read task: " + uuid, e);
        }
    }

}
