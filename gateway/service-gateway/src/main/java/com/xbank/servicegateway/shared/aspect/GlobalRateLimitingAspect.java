package com.xbank.servicegateway.shared.aspect;//package com.xbank.servicegateway.shared.aspect;
//
////import com.example.rateLimiter.annotation.RateLimited;
////import com.example.rateLimiter.exception.RateLimitExceededException;
////import com.example.rateLimiter.util.JwtUtil;
//import com.xbank.servicegateway.shared.Exceptions.RateLimitExceededException;
//import com.xbank.servicegateway.shared.annotations.RateLimited;
//import com.xbank.servicegateway.shared.service.JwtClient;
//import com.xbank.servicegateway.shared.service.RateLimiter;
//import jakarta.servlet.http.HttpServletRequest;
////import org.aspectj.lang.annotation.Aspect;
////import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.redisson.api.RRateLimiter;
//import org.redisson.api.RateIntervalUnit;
////import org.redisson.api.RateLimiter;
//import org.redisson.api.RedissonClient;
//import org.redisson.api.RateType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
////import javax.servlet.http.HttpServletRequest;
//
//@Aspect
//@Component
//public class GlobalRateLimitingAspect {
//    private final RateLimiter rateLimiter;
//    private final JwtClient jwtUtil;
//    private final HttpServletRequest request;
//
//    @Autowired
//    public GlobalRateLimitingAspect(RateLimiter rateLimiter, JwtClient jwtUtil, HttpServletRequest request) {
//        this.rateLimiter = rateLimiter;
//        this.jwtUtil = jwtUtil;
//        this.request = request;
//    }
//
//    @Before("@annotation(rateLimited)")
//    public void beforeRequest(RateLimited rateLimited) {
////        String requestBody = (String) RequestContextHolder.getRequestAttributes()
////                .getAttribute("cachedRequestBody", RequestAttributes.SCOPE_REQUEST);
////        System.out.println(":::::::::::"+ requestBody);
//        System.out.println("----------------------------------------");
//        String clientId = jwtUtil.getClientId(request);
//        String endpoint = rateLimited.endpoint();
//        Integer point = rateLimited.point();
//        long interval = rateLimited.interval();
//        RateIntervalUnit intervalUnit = rateLimited.unit();
//
//        String key = clientId + ":" + endpoint;
//        System.out.println("::::::::::::::::::::: "+ key);
//        this.rateLimiter.exec(key, interval, intervalUnit, point);
////        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
////        rateLimiter.trySetRate(RateType.OVERALL, 1, interval, intervalUnit);
////
////        if (!rateLimiter.tryAcquire()) {
////            throw new RateLimitExceededException("Rate limit exceeded");
////        }
//    }
//}

import com.xbank.servicegateway.shared.annotations.RateLimited;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class GlobalRateLimitingAspect {

    private final RateLimiterRegistry rateLimiterRegistry;

//    @Autowired
//    public RateLimiterAspect(RateLimiterRegistry rateLimiterRegistry) {
//        this.rateLimiterRegistry = rateLimiterRegistry;
//    }

    @Autowired
    public GlobalRateLimitingAspect(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @Before("@annotation(rateLimited)")
    public void applyRateLimiter(JoinPoint joinPoint, RateLimited rateLimited) {
        String name = rateLimited.name();
        int limitForPeriod = rateLimited.limitForPeriod();
        Duration limitRefreshPeriod = Duration.ofSeconds(Long.parseLong(rateLimited.limitRefreshPeriod()));
        Duration timeoutDuration = Duration.ofMillis(Long.parseLong(rateLimited.timeoutDuration()));
        System.out.println(timeoutDuration + " ... " + limitRefreshPeriod);
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(limitRefreshPeriod)
                .timeoutDuration(timeoutDuration)
                .build();

        // Create a new RateLimiter with updated config
        RateLimiter rateLimiter = RateLimiter.of(name, config);

        // Register or replace the RateLimiter in the registry
        rateLimiterRegistry.replace(name, rateLimiter);
    }
}
