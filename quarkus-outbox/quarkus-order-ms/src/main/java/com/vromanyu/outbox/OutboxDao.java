package com.vromanyu.outbox;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class OutboxDao {

    @Inject
    EntityManager em;

    public Outbox create(Outbox outbox) {
        em.persist(outbox);
        return outbox;
    }

    public List<Outbox> findFirst10Unprocessed(){
        TypedQuery<Outbox> query = em.createQuery("select o from Outbox o where o.processed = '0' order by o.createdAt", Outbox.class);
        query.setMaxResults(10);
        return query.getResultList();
    }

    public void update(Outbox outbox) {
        em.merge(outbox);
    }
}
