package com.example.ecommerce.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setKey(String key, String value, long timeoutMillis) {
        redisTemplate.opsForValue().set(key, value, timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public String getKey(String key, long waitTimeInSeconds) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < waitTimeInSeconds*1000) {  // 2 seconds wait time
                String value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    return value;  // Return value if found
                }
                try {
                    Thread.sleep(100);  // Check every 100ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return null; // Timeout reached, return null
        }).join();  // Wait for completion and return the result
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
