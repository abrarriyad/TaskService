package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String id) {

        log.debug("Received request for task: {}", id);

        try {
            TaskDto taskDto = taskService.getTask(id);
            log.info("Task found: {}",id);
            return ResponseEntity.ok(TaskResponse.success(taskDto));

        } catch (TaskNotFoundException e) {
            log.debug("Task not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
