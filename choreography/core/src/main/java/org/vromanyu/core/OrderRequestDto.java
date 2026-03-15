package org.vromanyu.core;

public record OrderRequestDto(Integer userId,
                              Integer productId,
                              Integer amount,
                              Integer orderId) {
}
