package com.vromanyu.order;

import com.vromanyu.dto.CreateOrderRequestDto;
import com.vromanyu.dto.CreateOrderResponseDto;
import com.vromanyu.event.OrderCreatedEvent;
import com.vromanyu.outbox.Outbox;
import com.vromanyu.outbox.OutboxDao;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderDao orderDao;

    @Inject
    OutboxDao outboxDao;

    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto request) {
        Order order = OrderMapper.toOrder(request);
        order.orderUuid = UUID.randomUUID().toString();
        Order createdOrder = orderDao.create(order);
        Log.infof("order created: %s", createdOrder);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(UUID.randomUUID().toString(),
                createdOrder.orderUuid,
                createdOrder.itemName,
                createdOrder.productType,
                createdOrder.quantity,
                createdOrder.price.doubleValue(),
                createdOrder.createdAt);

        Outbox outbox = new Outbox();
        outbox.setOrderId(createdOrder.id);
        outbox.setProcessed("0");
        outbox.setPayload(orderCreatedEvent);
        outboxDao.create(outbox);
        Log.infof("outbox created: %s", outbox);

        return OrderMapper.toOrderResponse(createdOrder);
    }
}
