package org.vromanyu.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.order.entity.Order;
import org.vromanyu.order.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequestDto orderRequest) {
        Order savedOrder = orderRepository.save(toOrder(orderRequest));
        orderPublisher.publishOrderCreatedEvent(savedOrder.getId(), orderRequest);
        return savedOrder;
    }

    @Override
    @Transactional
    public Order getOrder(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order " + orderId + " not found"));
    }

    private Order toOrder(OrderRequestDto orderRequest) {
        Order order = new Order();
        order.setProductId(orderRequest.productId());
        order.setUserId(orderRequest.userId());
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPrice(orderRequest.amount());
        return order;
    }
}
