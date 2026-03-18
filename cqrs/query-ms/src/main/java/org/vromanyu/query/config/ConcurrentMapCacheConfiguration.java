package org.vromanyu.query.config;

import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("!prod")
public class ConcurrentMapCacheConfiguration {

    @Bean
    public CacheManager ConcurrentMapcacheManager() {
        return new ConcurrentMapCacheManager("single-product", "all-products");
    }

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return cacheManager -> cacheManager.setAllowNullValues(false);
    }
}
