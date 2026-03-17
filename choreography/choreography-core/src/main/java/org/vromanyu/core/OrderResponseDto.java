package org.vromanyu.core;

public record OrderResponseDto(Integer userId,
                               Integer productId,
                               Integer amount,
                               Integer orderId,
                               OrderStatus orderStatus) {
}
