package com.example.taskservice.dto;

public record TaskResponse(
        TaskDto data,
        String status
) {

    public static TaskResponse success(TaskDto task){
        return new TaskResponse(task, "success");
    }
}
