package com.vromanyu.event;

import java.time.Instant;

public record OrderCreatedEvent(String eventId,
                                String orderUuid,
                                String itemName,
                                String productType,
                                int quantity,
                                double price,
                                Instant createdAt) {
}
