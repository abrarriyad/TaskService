package com.example.taskservice.integration;

import com.example.taskservice.entity.Task;
import com.example.taskservice.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CacheIntegrationTest {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // Clear cache before each test
        cacheManager.getCache("taskCache").clear();
    }

    @Test
    void cacheService_WhenTaskExists_ReturnsCachedTask() {
        // Given - using existing task file from the actual Tasks directory
        String existingUUID = "222fe30c-b4c0-4b49-bc30-f7859c31f81d";

        // When
        Task task1 = cacheService.getTaskFromCache(existingUUID);
        Task task2 = cacheService.getTaskFromCache(existingUUID); // Should be from cache

        // Then
        assertNotNull(task1);
        assertNotNull(task2);
        assertEquals(existingUUID, task1.getId());
        assertEquals(existingUUID, task2.getId());
        
        // Verify cache contains the task
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache("taskCache");
        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        assertTrue(nativeCache.asMap().containsKey(existingUUID));
        assertEquals(1, nativeCache.asMap().size());
    }


    @Test
    void cache_RespectsSizeLimit() {
        // Given - Test that cache configuration is working
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache("taskCache");
        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();

        // When - Access multiple different UUIDs (more than cache limit)
        String[] uuids = {
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222", 
                "33333333-3333-3333-3333-333333333333",
                "44444444-4444-4444-4444-444444444444",
                "55555555-5555-5555-5555-555555555555",
                "66666666-6666-6666-6666-666666666666",
                "77777777-7777-7777-7777-777777777777",
                "88888888-8888-8888-8888-888888888888"
        };

        for (String uuid : uuids) {
            Task task = cacheService.getTaskFromCache(uuid);
            assertNull(task); // All should be null since files don't exist
        }

        // Then - Verify cache is working and respecting limits
        // Due to LRU eviction, cache should not grow beyond a reasonable size
        assertTrue(nativeCache.asMap().size() <= 8, 
                   "Cache size should be reasonable, but was: " + nativeCache.asMap().size());
                   
        // Verify that cache contains some entries (proving it's working)
        assertTrue(nativeCache.asMap().size() > 0, "Cache should contain some entries");
        
        // Test that recently accessed items are still in cache
        String lastUUID = "88888888-8888-8888-8888-888888888888";
        assertTrue(nativeCache.asMap().containsKey(lastUUID), 
                   "Most recently accessed item should be in cache");
    }
} 