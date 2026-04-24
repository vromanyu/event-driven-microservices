package com.vromanyu.order;

import com.vromanyu.dto.CreateOrderRequestDto;
import com.vromanyu.dto.CreateOrderResponseDto;

public class OrderMapper {
    private OrderMapper() {

    }

    public static Order toOrder(CreateOrderRequestDto dto) {
        Order order = new Order();
        order.itemName = dto.itemName();
        order.productType = dto.productType();
        order.price = dto.price();
        order.quantity = dto.quantity();
        return order;
    }

    public static CreateOrderResponseDto toOrderResponse(Order order) {
        return new CreateOrderResponseDto(order.orderUuid,
                order.itemName,
                order.productType,
                order.quantity,
                order.price,
                order.createdAt);
    }
}
