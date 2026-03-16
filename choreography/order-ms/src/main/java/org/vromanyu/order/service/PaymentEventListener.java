package org.vromanyu.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.core.PaymentEvent;
import org.vromanyu.core.PaymentRequestDto;
import org.vromanyu.core.PaymentStatus;
import org.vromanyu.order.entity.Order;
import org.vromanyu.order.repository.OrderRepository;

@Service
@KafkaListener(topics = "${kafka.topics.payment}")
public class PaymentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventListener.class);
    private final OrderRepository orderRepository;

    public PaymentEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaHandler
    @Transactional
    public void handlePaymentSuccessfulEvent(@Payload PaymentEvent paymentEvent) {
        logger.info("received payment event: {}", paymentEvent);
        PaymentStatus paymentStatus = paymentEvent.paymentStatus();
        PaymentRequestDto paymentRequest = paymentEvent.paymentRequestDto();
        if (paymentStatus.equals(PaymentStatus.PAID)) {
            Order order = orderRepository.findById(paymentRequest.orderId()).orElseThrow(() -> new RuntimeException("order " + paymentRequest.orderId() + " not found"));
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            logger.info("order {} is paid", order.getId());
        } else if (paymentStatus.equals(PaymentStatus.FAILED)) {
            Order order = orderRepository.findById(paymentRequest.orderId()).orElseThrow(() -> new RuntimeException("order " + paymentRequest.orderId() + " not found"));
            order.setPaymentStatus(PaymentStatus.FAILED);
            order.setOrderStatus(OrderStatus.CANCELLED);
            logger.info("order {} is failed", order.getId());
        }
    }
}
