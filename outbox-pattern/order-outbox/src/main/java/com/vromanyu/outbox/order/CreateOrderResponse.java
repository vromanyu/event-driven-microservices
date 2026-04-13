package com.vromanyu.outbox.order;

import java.time.OffsetDateTime;

public record CreateOrderResponse(long orderId, long userId, double amount, OffsetDateTime createdAt) {
}
