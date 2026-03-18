package org.vromanyu.query.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.GetProductResponse;
import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;

@Service
@KafkaListener(topics = "${kafka.topics.product}", groupId = "query-ms")
public class ProductEventConsumerServiceImpl implements ProductEventConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventConsumerServiceImpl.class);

    private final ProductRepository productRepository;

    private final CacheManager cacheManager;

    public ProductEventConsumerServiceImpl(ProductRepository productRepository, CacheManager cacheManager) {
        this.productRepository = productRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @KafkaHandler
    @Transactional
    @CacheEvict(value = "all-products", allEntries = true)
    public void consumeProductCreatedEvent(@Payload ProductCreatedEvent productCreatedEvent) {
        logger.info("received product created event: {}", productCreatedEvent);
        Product product = new Product(productCreatedEvent.productId(),
                productCreatedEvent.productName(),
                productCreatedEvent.price(),
                productCreatedEvent.quantity());
        Product savedProduct = productRepository.save(product);
        logger.info("product saved: {}", savedProduct);

        /* Because the controller method returns GetProductResponse,
        we need to cache the new product in the correct format.*/
        logger.info("caching newly created product: {} ", savedProduct);
        Cache singleProductCache = cacheManager.getCache("single-product");
        if (singleProductCache != null) {
            singleProductCache.put(savedProduct.getId(), new GetProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getQuantity()));
        }
    }

    @Override
    @KafkaHandler
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "all-products", allEntries = true),
                    @CacheEvict(value = "single-product", key = "#productUpdatedEvent.productId()")
            }
    )
    public void consumeProductUpdatedEvent(@Payload ProductUpdatedEvent productUpdatedEvent) {
        logger.info("received product updated event: {}", productUpdatedEvent);
        Product product = productRepository.findById(productUpdatedEvent.productId())
                .orElseThrow(() -> new RuntimeException("product with id " + productUpdatedEvent.productId() + " not found"));
        product.setName(productUpdatedEvent.productName());
        product.setPrice(productUpdatedEvent.price());
        product.setQuantity(productUpdatedEvent.quantity());
        Product updatedProduct = productRepository.save(product);
        logger.info("product updated: {}", updatedProduct);

        /* Because the controller method returns GetProductResponse,
        we need to cache the new product in the correct format.*/
        logger.info("caching updated created product: {} ", updatedProduct);
        Cache singleProductCache = cacheManager.getCache("single-product");
        if (singleProductCache != null) {
            singleProductCache.put(updatedProduct.getId(), new GetProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice(), updatedProduct.getQuantity()));
        }
    }

    @Override
    @KafkaHandler
    @Transactional
    @Caching(
            evict = {@CacheEvict(value = "all-products", allEntries = true),
                    @CacheEvict(value = "single-product", key = "#productDeletedEvent.productId()")
            }
    )
    public void consumeProductDeletedEvent(@Payload ProductDeletedEvent productDeletedEvent) {
        logger.info("received product deleted event: {}", productDeletedEvent);
        Product product = productRepository.findById(productDeletedEvent.productId())
                .orElseThrow(() -> new RuntimeException("product with id " + productDeletedEvent.productId() + " not found"));
        productRepository.delete(product);
        logger.info("product deleted");
    }

}
