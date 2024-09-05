package com.xbank.servicegateway.config;

import lombok.Getter;
import redis.clients.jedis.Jedis;
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
//        poolConfig.setMinEvictableIdleTimeMillis(60000);
//        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        System.out.println("******************************");
        try {
            this.jedisPool = new JedisPool(poolConfig, "localhost", 6379, 2000);
        }catch(Exception e){
            System.out.println("--------------------------****----");
            System.out.println(e);
            throw e;
        }
//        System.out.println(this.jedisPool+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        try (Jedis jedis = this.jedisPool.getResource()) {
            // Use the jedis instance as you would normally do
            jedis.set("mykey", "myvalue");

            String value = jedis.get("mykey");
            System.out.println("Stored value in Redis: " + value);
        }

    }

}
