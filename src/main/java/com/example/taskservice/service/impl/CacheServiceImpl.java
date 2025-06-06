package com.example.taskservice.service.impl;

import com.example.taskservice.entity.Task;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final TaskRepository taskRepository;
    @Override
    @Cacheable(value = "taskCache", key = "#id")
    public Task getTaskFromCache(String id) {
        log.info("Cache MISS - fetching from disk for id: " + id);
        return taskRepository.findByUUID(id).orElse(null);
    }
}
