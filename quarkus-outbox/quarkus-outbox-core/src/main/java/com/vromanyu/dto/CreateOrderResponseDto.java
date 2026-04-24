package com.vromanyu.dto;

import java.time.Instant;

public record CreateOrderResponseDto(String orderUuid, String itemName, String productType, int quantity, long price, Instant createdAt) {
}
