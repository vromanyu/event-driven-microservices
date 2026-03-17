package org.vromanyu.core;

public record UpdateProductRequest(String productName, Integer price, Integer quantity) {
}
