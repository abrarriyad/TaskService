package com.example.taskservice;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.registerCustomCache("taskCache",
                Caffeine.newBuilder()
                        .maximumSize(5)
                        .expireAfterWrite(Duration.ofHours(1))
                        .build());

        return cacheManager;
    }
}
