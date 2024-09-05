package com.xbank.servicegateway.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RedissonConfig {
//
//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;
//
////    @Bean(destroyMethod = "shutdown")
//    public Config redissonClient() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379");
//        return config;
//    }
//}