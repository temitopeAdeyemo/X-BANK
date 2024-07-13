package com.xbank.servicegateway.shared.aspect;

//import com.example.rateLimiter.annotation.RateLimited;
//import com.example.rateLimiter.exception.RateLimitExceededException;
//import com.example.rateLimiter.util.JwtUtil;
import com.xbank.servicegateway.shared.Exceptions.RateLimitExceededException;
import com.xbank.servicegateway.shared.annotations.RateLimited;
import com.xbank.servicegateway.shared.service.JwtClient;
import com.xbank.servicegateway.shared.service.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
//import org.redisson.api.RateLimiter;
import org.redisson.api.RedissonClient;
import org.redisson.api.RateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class GlobalRateLimitingAspect {

    private final RateLimiter rateLimiter;
    private final JwtClient jwtUtil;
    private final HttpServletRequest request;

    @Autowired
    public GlobalRateLimitingAspect(RateLimiter rateLimiter, JwtClient jwtUtil, HttpServletRequest request) {
        this.rateLimiter = rateLimiter;
        this.jwtUtil = jwtUtil;
        this.request = request;
    }

    @Before("@annotation(rateLimited)")
    public void beforeRequest(RateLimited rateLimited) {
        String clientId = jwtUtil.getClientId(request);
        String endpoint = rateLimited.endpoint();
        long interval = rateLimited.interval();
        RateIntervalUnit intervalUnit = rateLimited.unit();

        String key = clientId + ":" + endpoint;
        this.rateLimiter.exec(key, interval, intervalUnit);
//        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
//        rateLimiter.trySetRate(RateType.OVERALL, 1, interval, intervalUnit);
//
//        if (!rateLimiter.tryAcquire()) {
//            throw new RateLimitExceededException("Rate limit exceeded");
//        }
    }
}