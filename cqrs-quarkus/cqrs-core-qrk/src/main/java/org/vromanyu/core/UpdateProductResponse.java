package org.vromanyu.core;

public record UpdateProductResponse(Integer productId, String productName, Integer price, Integer quantity) {
}
