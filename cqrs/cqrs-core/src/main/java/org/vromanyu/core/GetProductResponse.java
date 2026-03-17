package org.vromanyu.core;

public record GetProductResponse(Integer productId, String productName, Integer price, Integer quantity) {
}
