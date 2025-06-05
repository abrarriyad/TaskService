package com.example.taskservice.mapper;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.entity.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDto(task.getId(), task.getDescription());
    }

}
