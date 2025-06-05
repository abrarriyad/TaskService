package com.example.taskservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Task(
        @JsonProperty("Id")
        String id,
        String description
) {
}
