package org.vromanyu.query.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;

@Service
@KafkaListener(topics = "${kafka.topics.product}", groupId = "query-ms")
public class ProductEventConsumerServiceImpl implements ProductEventConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventConsumerServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductEventConsumerServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @KafkaHandler
    @Transactional
    public void consumeProductCreatedEvent(@Payload ProductCreatedEvent productCreatedEvent) {
        logger.info("received product created event: {}", productCreatedEvent);
        Product product = new Product(productCreatedEvent.productId(),
                productCreatedEvent.productName(),
                productCreatedEvent.price(),
                productCreatedEvent.quantity());
        productRepository.save(product);
        logger.info("product saved: {}", product);
    }

    @Override
    @KafkaHandler
    public void consumeProductUpdatedEvent(@Payload ProductUpdatedEvent productUpdatedEvent) {
        logger.info("received product updated event: {}", productUpdatedEvent);
        Product product = productRepository.findById(productUpdatedEvent.productId())
                .orElseThrow(() -> new RuntimeException("product with id " + productUpdatedEvent.productId() + " not found"));
        product.setName(productUpdatedEvent.productName());
        product.setPrice(productUpdatedEvent.price());
        product.setQuantity(productUpdatedEvent.quantity());
        productRepository.save(product);
        logger.info("product updated: {}", product);
    }

    @Override
    @KafkaHandler
    public void consumeProductDeletedEvent(@Payload ProductDeletedEvent productDeletedEvent) {
        logger.info("received product deleted event: {}", productDeletedEvent);
        Product product = productRepository.findById(productDeletedEvent.productId())
                .orElseThrow(() -> new RuntimeException("product with id " + productDeletedEvent.productId() + " not found"));
        productRepository.delete(product);
        logger.info("product deleted");
    }

}
