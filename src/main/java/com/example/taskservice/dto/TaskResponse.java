package com.example.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task response object containing task details")
public record TaskResponse(
        @Schema(
            description = "Unique identifier of the task",
            example = "123e4567-e89b-12d3-a456-426614174000",
            pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        )
        @JsonProperty("Id")
        String id,
        
        @Schema(
            description = "Description of the task",
            example = "Complete project documentation and API testing"
        )
        String description
) {
}
