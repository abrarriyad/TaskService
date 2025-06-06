package com.example.taskservice.service;

import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;

public interface TaskService {
    Task getTask(String id) throws TaskNotFoundException;
}
