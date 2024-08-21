package com.xbankuser.userservice.shared.service.cache;
import com.xbankuser.userservice.shared.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;


    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    /**
     * Save the key value pair in cache with a ttl
     *
     * @param key   cache key
     * @param value cache value
     */
    public void set(String key, Object data, long timeout, TimeUnit unit) {
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
    public Object get(String key) {
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