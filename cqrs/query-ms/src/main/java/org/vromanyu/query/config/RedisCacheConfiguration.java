package org.vromanyu.query.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
@Profile("prod")
public class RedisCacheConfiguration {

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        org.springframework.data.redis.cache.RedisCacheConfiguration allProductsCacheConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        org.springframework.data.redis.cache.RedisCacheConfiguration singleProductCacheConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("single-product", singleProductCacheConfiguration)
                .withCacheConfiguration("all-products", allProductsCacheConfiguration)
                .enableStatistics().
                build();
    }
}
