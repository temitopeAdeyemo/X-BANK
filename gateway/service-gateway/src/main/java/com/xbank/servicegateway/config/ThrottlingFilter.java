package com.xbank.servicegateway.config;

import com.xbank.servicegateway.shared.service.RateLimiterr;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(1)
public class ThrottlingFilter extends OncePerRequestFilter {
    private final RateLimiterr rateLimiterr;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userToken = request.getHeader("Authorization"); // Assuming the token is passed in the Authorization header

        String key = "global_limiter_key";
        long tokenCount = 100;
        long duration = 120;

        if(!(userToken == null) && request.getRequestURI().contains("/transfer")) {
            key = request.getPathInfo()+"/"+userToken;
            tokenCount = 2;
            duration = 5;
        }

        if ( !rateLimiterr.tryConsume(key, tokenCount, duration,1)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
