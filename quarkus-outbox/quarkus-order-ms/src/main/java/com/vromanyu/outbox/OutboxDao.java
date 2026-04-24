package com.vromanyu.outbox;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class OutboxDao {

    @Inject
    EntityManager em;

    public Outbox create(Outbox outbox) {
        em.persist(outbox);
        return outbox;
    }
}
