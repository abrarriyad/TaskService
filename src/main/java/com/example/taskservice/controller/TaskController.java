package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.entity.Task;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    private final TaskService taskService;


    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable 
            @Pattern(regexp = UUID_REGEX, message = "Invalid UUID format") 
            String id) {
        
        log.debug("Received request for task: {}", id);
        
        Task task = taskService.getTask(id);
        TaskResponse taskResponse = TaskMapper.toResponse(task);
        log.info("Task found: {}", id);
        return ResponseEntity.ok(taskResponse);
    }
}
