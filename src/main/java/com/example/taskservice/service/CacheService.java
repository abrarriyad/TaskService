package com.example.taskservice.service;

import com.example.taskservice.entity.Task;

import java.util.Optional;

public interface CacheService {
    Task getTaskFromCache(String id);
}
