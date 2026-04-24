package com.vromanyu.order;

import com.vromanyu.dto.CreateOrderRequestDto;
import com.vromanyu.dto.CreateOrderResponseDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderDao orderDao;

    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto request) {
        Order order = OrderMapper.toOrder(request);
        order.orderUuid = UUID.randomUUID().toString();
        Order createdOrder = orderDao.create(order);
        return OrderMapper.toOrderResponse(createdOrder);
    }
}
