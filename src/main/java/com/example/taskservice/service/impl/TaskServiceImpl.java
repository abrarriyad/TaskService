package com.example.taskservice.service.impl;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.CacheService;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService
{
    private final CacheService cacheService;
    @Override
    public TaskDto getTask(String id) throws TaskNotFoundException {
        return Optional.ofNullable(cacheService.getTaskFromCache(id))
                .map(TaskMapper::toDto)
                .orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
    }
}
