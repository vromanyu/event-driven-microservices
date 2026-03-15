package org.vromanyu.core;

public record PaymentRequestDto(Integer orderId,
                                Integer userId,
                                Integer amount) {
}
