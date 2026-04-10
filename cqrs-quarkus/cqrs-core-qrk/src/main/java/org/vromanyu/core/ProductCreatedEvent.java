package org.vromanyu.core;

public record ProductCreatedEvent(String eventId,
                                  Integer productId,
                                  String productName,
                                  Integer price,
                                  Integer quantity) {
}
