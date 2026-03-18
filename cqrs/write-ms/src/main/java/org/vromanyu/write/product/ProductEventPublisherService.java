package org.vromanyu.write.product;

import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;

public interface ProductEventPublisherService {
    void publishProductCreatedEvent(ProductCreatedEvent productCreatedEvent);

    void publishProductUpdatedEvent(ProductUpdatedEvent productUpdatedEvent);

    void publishProductDeletedEvent(ProductDeletedEvent productDeletedEvent);
}
