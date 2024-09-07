package com.xbank.servicegateway.config;

import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter

public class JedisConfig {
    private final JedisPool jedisPool;

    public JedisConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        try {
            this.jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000);
        }catch(Exception e){
            System.out.println(e);
            throw e;
        }
    }

}
