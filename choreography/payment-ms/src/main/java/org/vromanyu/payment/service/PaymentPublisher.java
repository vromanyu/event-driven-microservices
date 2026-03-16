package org.vromanyu.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.OrderEvent;
import org.vromanyu.core.PaymentEvent;
import org.vromanyu.core.PaymentRequestDto;
import org.vromanyu.core.PaymentStatus;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PaymentPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.payment}")
    private String paymentTopic;

    public PaymentPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendFailedEvent(PaymentRequestDto paymentRequestDto, OrderEvent orderEvent) {
        PaymentEvent paymentEvent = new PaymentEvent(
                UUID.randomUUID().toString(),
                LocalDate.now(),
                paymentRequestDto,
                PaymentStatus.FAILED);
        kafkaTemplate.send(paymentTopic, orderEvent.orderId().toString(), paymentEvent);
    }
}
