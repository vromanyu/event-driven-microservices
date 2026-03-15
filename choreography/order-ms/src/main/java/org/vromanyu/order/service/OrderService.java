package org.vromanyu.order.service;

import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.order.entity.Order;

public interface OrderService {
    Order createOrder(OrderRequestDto orderRequest);

    Order getOrder(Integer orderId);
}
