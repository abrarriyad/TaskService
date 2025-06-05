package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService
{
    private final TaskRepository taskRepository;
    @Override
    public TaskDto getTask(String id) throws TaskNotFoundException {

        Optional<Task> task = taskRepository.findByUUID(id);

        return task.map(TaskMapper::toDto).
                orElseThrow(()-> new TaskNotFoundException("Task Not Found"));
    }
}
