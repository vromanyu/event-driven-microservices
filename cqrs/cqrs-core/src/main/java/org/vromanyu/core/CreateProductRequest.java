package org.vromanyu.core;

public record CreateProductRequest(String productName, Integer price, Integer quantity) {
}
