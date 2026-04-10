package org.vromanyu.core;

public record ProductUpdatedEvent(String eventId,
                                  Integer productId,
                                  String productName,
                                  Integer price,
                                  Integer quantity) {
}
