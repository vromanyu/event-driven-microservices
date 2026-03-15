package org.vromanyu.core;

import java.time.LocalDate;

public record PaymentEvent(String eventId,
                           LocalDate date,
                           PaymentRequestDto paymentRequestDto,
                           PaymentStatus paymentStatus) {
}
