package com.example.taskservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache.task.maximum-size:5}")
    private int cacheMaximumSize;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.registerCustomCache("taskCache",
                Caffeine.newBuilder()
                        .maximumSize(cacheMaximumSize)
                        .recordStats()
                        .build());

        return cacheManager;
    }
}
