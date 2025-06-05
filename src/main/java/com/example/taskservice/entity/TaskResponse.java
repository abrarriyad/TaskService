package com.example.taskservice.entity;

public record TaskResponse(
        Task data,
        String status
) {

    public static TaskResponse success(Task task){
        return new TaskResponse(task, "success");
    }
}
