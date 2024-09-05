package com.xbank.servicegateway.shared.service;

import com.xbank.servicegateway.config.JedisConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.jedis.Bucket4jJedis;
import io.github.bucket4j.redis.jedis.cas.JedisBasedProxyManager;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiterr {
    public JedisBasedProxyManager<String> proxyManager;
    public RateLimiterr() {
        redis.clients.jedis.JedisPool jedisPool = new JedisConfig().getJedisPool();
        this.proxyManager = Bucket4jJedis.casBasedBuilder(jedisPool)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .keyMapper(Mapper.BYTES.STRING)
                .build();
    }

    private Bucket createBucket(String key, long token, long durationInSec){
        var configuration = BucketConfiguration.builder().addLimit((bandwidthBuilderCapacityStage ->
            bandwidthBuilderCapacityStage.capacity(token).refillIntervally(token, Duration.ofSeconds(durationInSec))
        )).build();
        return proxyManager.getProxy(key, () -> configuration);
    }

    public boolean tryConsume(String userToken, long token, long durationInSec, long tokenConsumeCount) {
        Bucket bucket = createBucket(userToken, token, durationInSec);
        return bucket.tryConsume(tokenConsumeCount);
    }
}