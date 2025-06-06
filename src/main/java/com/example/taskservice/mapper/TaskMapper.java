package com.example.taskservice.mapper;

import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.entity.Task;

public class TaskMapper {

    public static TaskResponse toResponse(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskResponse(task.getId(), task.getDescription());
    }

}
