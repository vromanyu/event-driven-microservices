package com.vromanyu.outbox.order;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "order", schema = "order_outbox")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    Double amount;

    @Column(nullable = false)
    Boolean isProcessed;

    @CreationTimestamp
    OffsetDateTime createdAt;

    @UpdateTimestamp
    OffsetDateTime updatedAt;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "outbox_id")
    OrderOutbox orderOutbox;

    void setOrderOutbox(OrderOutbox orderOutbox) {
        this.orderOutbox = orderOutbox;
        if (orderOutbox != null) {
            orderOutbox.order = this;
        }
    }
}
