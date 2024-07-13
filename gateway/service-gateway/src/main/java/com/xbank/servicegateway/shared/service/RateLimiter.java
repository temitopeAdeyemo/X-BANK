package com.xbank.servicegateway.shared.service;

import com.xbank.servicegateway.shared.Exceptions.RateLimitExceededException;
import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimiter {
    private final RedissonClient redissonClient;

    @Autowired
    RateLimiter(RedissonClient redisson){
        this.redissonClient = redisson;
    }
    public void exec(String key ,long interval, RateIntervalUnit intervalUnit){
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 1, interval, intervalUnit);

        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitExceededException("Rate limit exceeded");
        }
    }
}

