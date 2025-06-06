package com.example.taskservice.service.impl;

import com.example.taskservice.entity.LRUCache;
import com.example.taskservice.entity.Task;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.repository.TaskRepository;
import com.example.taskservice.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("lruCacheService")
@RequiredArgsConstructor
public class LRUCacheService implements CacheService {
    private final  LRUCache<String, Task> lruCache;
    private final TaskRepository taskRepository;
    @Override
    public Task getTaskFromCache(String id) {

        return lruCache.get(id)
                    .orElseGet(()->{
                       Task task = taskRepository.findByUUID(id)
                               .orElseThrow(()-> new TaskNotFoundException("Task NOt found"));

                       lruCache.put(task.getId(),task);
                       return task;
                    });
    }
}
