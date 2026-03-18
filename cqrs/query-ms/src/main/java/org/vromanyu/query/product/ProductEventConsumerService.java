package org.vromanyu.query.product;

import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;

public interface ProductEventConsumerService {
    void consumeProductCreatedEvent(ProductCreatedEvent productCreatedEvent);

    void consumeProductUpdatedEvent(ProductUpdatedEvent productUpdatedEvent);

    void consumeProductDeletedEvent(ProductDeletedEvent productDeletedEvent);
}
