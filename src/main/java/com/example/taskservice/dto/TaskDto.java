package com.example.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskDto(
        @JsonProperty("Id")
        String id,
        String description
) {
}
