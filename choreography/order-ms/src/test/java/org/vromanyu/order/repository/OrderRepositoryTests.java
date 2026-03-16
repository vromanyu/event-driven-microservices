package org.vromanyu.order.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.order.entity.Order;

import java.util.List;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void givenOrder_whenSave_thenSuccess() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPrice(100);

        Order savedOrder = orderRepository.saveAndFlush(order);

        Assertions.assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    public void givenOrder_whenSave_thenPopulateAuditFields() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPrice(100);

        Order savedOrder = orderRepository.saveAndFlush(order);

        Assertions.assertThat(savedOrder.getCreatedAt()).isNotNull();
        Assertions.assertThat(savedOrder.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenValidOrderId_whenFindBy_shouldReturnOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPrice(100);
        Order savedOrder = orderRepository.saveAndFlush(order);

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        Assertions.assertThat(foundOrder).isNotNull();
    }

    @Test
    public void givenInvalidOrderId_whenFindBy_shouldThrowException() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPrice(100);
        orderRepository.saveAndFlush(order);

        Assertions.assertThatThrownBy(() -> orderRepository.findById(0).orElseThrow());
    }

    @Test
    public void givenThreeOrders_whenFindAll_shouldReturnThreeOrders() {
        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.CREATED);
        order1.setPrice(100);
        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.CREATED);
        order2.setPrice(100);
        Order order3 = new Order();
        order3.setOrderStatus(OrderStatus.CREATED);
        order3.setPrice(100);
        List<Order> orders = List.of(order1, order2, order3);

        orderRepository.saveAllAndFlush(orders);
        orders = orderRepository.findAll();

        Assertions.assertThat(orders).isNotNull();
        Assertions.assertThat(orders).hasSize(3);
    }

}
