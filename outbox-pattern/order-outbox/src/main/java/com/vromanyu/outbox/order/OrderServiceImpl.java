package com.vromanyu.outbox.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderOutboxRepository orderOutboxRepository) {
        this.orderRepository = orderRepository;
        this.orderOutboxRepository = orderOutboxRepository;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        throw new UnsupportedOperationException();
    }
}
