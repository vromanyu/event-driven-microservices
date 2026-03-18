package org.vromanyu.write.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;

@Service
public class ProductEventPublisherServiceImpl implements ProductEventPublisherService {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventPublisherServiceImpl.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topics.product}")
    private String productTopic;

    public ProductEventPublisherServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public void publishProductCreatedEvent(ProductCreatedEvent productCreatedEvent) {
        logger.info("publishProductCreatedEvent called with productCreatedEvent: {}", productCreatedEvent);
        kafkaTemplate.send(productTopic, productCreatedEvent.productId().toString(), productCreatedEvent);
    }

    @Override
    @Transactional
    public void publishProductUpdatedEvent(ProductUpdatedEvent productUpdatedEvent) {
        logger.info("publishProductUpdatedEvent called with productUpdatedEvent: {}", productUpdatedEvent);
        kafkaTemplate.send(productTopic, productUpdatedEvent.productId().toString(), productUpdatedEvent);
    }

    @Override
    @Transactional
    public void publishProductDeletedEvent(ProductDeletedEvent productDeletedEvent) {
        logger.info("publishProductDeletedEvent called with productDeletedEvent: {}", productDeletedEvent);
        kafkaTemplate.send(productTopic, productDeletedEvent.productId().toString(), productDeletedEvent);
    }

}
