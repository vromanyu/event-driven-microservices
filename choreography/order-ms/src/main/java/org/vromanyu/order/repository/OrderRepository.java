package org.vromanyu.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vromanyu.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
