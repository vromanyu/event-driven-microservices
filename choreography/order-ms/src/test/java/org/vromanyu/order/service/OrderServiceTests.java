package org.vromanyu.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.order.entity.Order;
import org.vromanyu.order.repository.OrderRepository;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderPublisher orderPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void givenOrderRequest_whenCreateOrder_thenSuccess() {
        OrderRequestDto orderRequest = new OrderRequestDto(1, 1, 1);
        Order mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setUserId(1);
        mockOrder.setProductId(1);
        mockOrder.setPrice(1);
        mockOrder.setOrderStatus(OrderStatus.CREATED);
        mockOrder.setCreatedAt(LocalDateTime.now());
        mockOrder.setUpdatedAt(LocalDateTime.now());

        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(mockOrder);
        Mockito.doNothing().when(orderPublisher).publishOrderCreatedEvent(1, orderRequest);

        Order createdOrder = orderService.createOrder(orderRequest);
        Assertions.assertThat(createdOrder).isNotNull();
        Assertions.assertThat(createdOrder.getId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getUserId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getProductId()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getPrice()).isEqualTo(1);
        Assertions.assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        Assertions.assertThat(createdOrder.getCreatedAt()).isNotNull();
        Assertions.assertThat(createdOrder.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenOrderId_whenGetOrder_thenSuccess() {
        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setProductId(1);
        order.setPrice(1);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Mockito.when(orderRepository.findById(1)).thenReturn(java.util.Optional.of(order));

        Order foundOrder = orderService.getOrder(1);
        Assertions.assertThat(foundOrder).isNotNull();
        Assertions.assertThat(foundOrder.getId()).isEqualTo(1);
    }

    @Test
    public void givenInvalidOrderId_whenGetOrder_thenThrowException() {
        Mockito.when(orderRepository.findById(Mockito.anyInt())).thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.getOrder(1));
    }

}
