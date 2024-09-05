package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.UserDto;
//import com.xbank.servicegateway.shared.Exceptions.RateLimitExceededException;
//import com.xbank.servicegateway.shared.annotations.RateLimited;
import com.xbank.servicegateway.shared.annotations.RateLimited;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import io.github.resilience4j.core.EventPublisher;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.event.RateLimiterEvent;
//import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class Dfe {
//    public final RateLimiterRegistry rateLimiterRegistry;
//    private final RateLimiter rateLimiter;
//    public Dfe(RateLimiterRegistry rateLimiterRegistry, RateLimiter rateLimiter) {
//        this.rateLimiterRegistry = rateLimiterRegistry;
//        this.rateLimiter = rateLimiter;
//    }
//    @RateLimited(endpoint = "/api/v1/auth/c", interval = 15, point = 3)
    @GetMapping("/")
//    @RateLimited(name = "myServiced")
//    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "myServiced")
    ResponseEntity<ApiResponse<List<UserDto>>> getUsere(){
//        try {
//            RateLimiter.decorateCheckedSupplier(rateLimiter, this::getUsere).get();
//        } catch (Throwable e) {
//            System.out.println(e);
//            throw  new RateLimitExceededException("Rate limit exceeded");
//        }
        return new ResponseEntity<>(new ApiResponse<>( "Users fetched successfully", null), HttpStatus.OK);
    }

//    @PostConstruct
//    public void postConstraint(){
//        EventPublisher<RateLimiterEvent> eventPublisher = rateLimiterRegistry.rateLimiter("name2").getEventPublisher();
//
//        eventPublisher.onEvent(event -> System.out.println("eventPublisher.onEvent: "+ event + "--" + event.getEventType() +"..."+ event.getNumberOfPermits()));
//    }
}
