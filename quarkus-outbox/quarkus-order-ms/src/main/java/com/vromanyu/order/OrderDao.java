package com.vromanyu.order;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class OrderDao {

    @Inject
    EntityManager em;

    public Order create(Order order) {
        em.persist(order);
        return order;
    }
}
