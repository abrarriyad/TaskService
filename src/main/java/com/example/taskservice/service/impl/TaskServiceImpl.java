package com.example.taskservice.service.impl;

import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.service.CacheService;
import com.example.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final CacheService cacheService;

    public TaskServiceImpl(  @Qualifier("lruCacheService")CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Task getTask(String id) throws TaskNotFoundException {
        return Optional.ofNullable(cacheService.getTaskFromCache(id))
                .orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
    }
}
