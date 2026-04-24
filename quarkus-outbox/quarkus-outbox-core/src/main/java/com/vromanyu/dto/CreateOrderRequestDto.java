package com.vromanyu.dto;

public record CreateOrderRequestDto(String itemName, String productType, int quantity, double price) {
}
