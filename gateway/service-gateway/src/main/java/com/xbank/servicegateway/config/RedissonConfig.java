package com.xbank.servicegateway.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                /* Default Connection Pool Size is 24, more connection pool size allow higher concurrent operations.
                If the application has many simultaneous Redis operations, having 24 connections will handle the load better without queuing requests.
                 Also reduce latency.*/
                .setConnectionPoolSize(2)
                .setConnectionMinimumIdleSize(2);
        return Redisson.create(config);
    }
}