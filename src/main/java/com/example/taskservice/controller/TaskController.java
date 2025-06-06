package com.example.taskservice.controller;

import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.entity.Task;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Pattern;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Task Management", description = "API to Get tasks with LRU caching")
public class TaskController {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    private final TaskService taskService;

    @Operation(
        summary = "Retrieve a task by ID",
        description = "Fetches a task from the cache or file system using its UUID. " +
                     "If the task is not in cache, it will be loaded from the JSON file."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Task found and returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TaskResponse.class),
                examples = @ExampleObject(
                    name = "Task Example",
                    value = """
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "description": "Complete project documentation"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Task Not Found",
                    value = """
                        {
                            "error": "Task not found",
                            "message": "No task found with ID: 123e4567-e89b-12d3-a456-426614174000"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid UUID format",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Invalid UUID",
                    value = """
                        {
                            "error": "Validation failed",
                            "message": "Invalid UUID format"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Server Error",
                    value = """
                        {
                            "error": "Internal server error",
                            "message": "An unexpected error occurred while processing the request"
                        }
                        """
                )
            )
        )
    })
    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(
                description = "UUID of the task to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000",
                schema = @Schema(pattern = UUID_REGEX)
            )
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
