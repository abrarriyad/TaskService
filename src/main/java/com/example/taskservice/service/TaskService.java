package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.exception.TaskNotFoundException;

public interface TaskService {
    TaskDto getTask(String id) throws TaskNotFoundException;
}
