package org.vromanyu.payment.service;

import org.vromanyu.core.OrderEvent;

public interface PaymentService {
    void pay(OrderEvent orderEvent);
}
