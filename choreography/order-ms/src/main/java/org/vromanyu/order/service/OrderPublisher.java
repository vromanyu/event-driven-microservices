package org.vromanyu.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.OrderEvent;
import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.core.OrderStatus;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class OrderPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OrderPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topics.order}")
    private String orderTopic;

    public OrderPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void publishOrderCreatedEvent(Integer orderId, OrderRequestDto orderRequest) {
        OrderEvent orderEvent = new OrderEvent(
                UUID.randomUUID().toString(),
                orderId,
                LocalDate.now(),
                orderRequest,
                OrderStatus.CREATED
        );
        kafkaTemplate.send(orderTopic, orderId.toString(), orderEvent)
                .whenComplete((recordMetadata, throwable) -> {
                    if (throwable != null) {
                        logger.error("error publishing order created event", throwable);
                        throw new RuntimeException(throwable);
                    }
                    logger.info("order event ({}) published to {} - {}",
                            recordMetadata.getProducerRecord().value(),
                            recordMetadata.getRecordMetadata().topic(),
                            recordMetadata.getRecordMetadata().partition());
                });
    }
}
