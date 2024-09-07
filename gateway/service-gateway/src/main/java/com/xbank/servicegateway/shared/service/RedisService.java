package com.xbank.servicegateway.shared.service;
import com.xbank.servicegateway.shared.Exceptions.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService<T> {

    private final RedisTemplate<String, T > redisTemplate;
    private final ValueOperations<String, T > valueOps;


    @Autowired
    public RedisService(RedisTemplate<String, T > redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    /**
     * Save the key value pair in cache with a ttl
     *
     * @param key   cache key
     * @param data cache value
     */
    public void set(String key, T  data, long timeout, TimeUnit unit) {
        try {
            valueOps.set(key, data,timeout, unit );
        } catch (RuntimeException e) {
            throw new ServerErrorException("Error while saving to cache ");
        }
    }

    /**
     * Returns the cached value
     *
     * @param key cached key
     * @return cached value
     */
    public T  get(String key) {
        try {
            return valueOps.get(key);
        } catch (RuntimeException e) {
            throw new ServerErrorException("Error while retrieving data.");
        }
    }

    /**
     * Remove the cached value
     *
     * @param key cached key
     */
    public void remove(String key) {
        try {
            redisTemplate.delete(key);
        } catch (RuntimeException e) {
            throw new ServerErrorException("Error while removing data.");
        }
    }
}