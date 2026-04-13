package com.vromanyu.outbox.order;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);
}
