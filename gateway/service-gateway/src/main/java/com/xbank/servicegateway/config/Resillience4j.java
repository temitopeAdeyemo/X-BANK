//package com.xbank.servicegateway.shared.service;
//
////import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
//import io.github.resilience4j.core.EventPublisher;
//import io.github.resilience4j.ratelimiter.RateLimiterConfig;
//import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
//import io.github.resilience4j.ratelimiter.event.RateLimiterEvent;
////import org.redisson.api.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.time.Duration;
//
//@Configuration
//public class Resillience4j {
//
////    public final RateLimiterRegistry rateLimiterRegistry;
////
////    public RateLimiterr(RateLimiterRegistry rateLimiterRegistry) {
////        this.rateLimiterRegistry = rateLimiterRegistry;
////    }
////    private final RedissonClient redissonClient;
//
////    @Autowired
////    RateLimiter(RedissonClient redisson){
////        this.redissonClient = redisson;
////    }
//
//    @Bean
//    public io.github.resilience4j.ratelimiter.RateLimiter rateLimiter(){
////        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
//
//        RateLimiterConfig config = RateLimiterConfig.custom()
//                .limitRefreshPeriod(Duration.ofSeconds(500)) // 10-second window
//                .limitForPeriod(1) // Maximum 5 requests in the window
//                .timeoutDuration(Duration.ofMillis(1000)) // Wait period of 10 seconds if limit exceeded
//                .build();
//
//        // Create registry
//        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
//
//        return rateLimiterRegistry.rateLimiter("myServiced", config);
//
//
//
////        point = point.toString().isEmpty()? 5 : point;
////
////        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
////        System.out.println(RateType.OVERALL + ".." + point + ".." + interval + ".." + intervalUnit);
////        rateLimiter.trySetRate(RateType.OVERALL, point, interval, intervalUnit);
//
////        if (!rateLimiter.tryAcquire()) {
////            throw new RateLimitExceededException("Rate limit exceeded");
////        }
//    }
//}
//
