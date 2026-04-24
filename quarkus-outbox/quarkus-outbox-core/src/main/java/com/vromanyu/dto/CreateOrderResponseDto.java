package com.vromanyu.dto;

import java.time.Instant;

public record CreateOrderResponseDto(String orderUuid, String itemName, String productType, int quantity, double price, Instant createdAt) {
}
