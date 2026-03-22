package org.vromanyu.gateway.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    @Profile("!prod")
    public AsyncProxyManager<String> caffeineProxyManager() {
        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .initialCapacity(1);
        return new CaffeineProxyManager<String>(builder, Duration.ofMinutes(1)).asAsync();
    }

    @Bean
    @Profile("prod")
    public RedisClient redisClient() {
        return RedisClient.create(
                RedisURI.builder()
                        .withHost("redis")
                        .withPort(6379)
                        .build());
    }

    @Bean
    @Profile("prod")
    public AsyncProxyManager<String> redisProxyManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
        return LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.fixedTimeToLive(Duration.ofMinutes(1)))
                .build()
                .asAsync();
    }
}

