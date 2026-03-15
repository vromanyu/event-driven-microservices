package org.vromanyu.core;

import java.time.LocalDate;

public record OrderEvent(String eventId,
                         LocalDate date,
                         OrderRequestDto orderRequestDto,
                         OrderStatus orderStatus) {
}
