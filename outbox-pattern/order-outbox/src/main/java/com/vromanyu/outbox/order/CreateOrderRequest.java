package com.vromanyu.outbox.order;

public record CreateOrderRequest(long userId, double amount) {
}
