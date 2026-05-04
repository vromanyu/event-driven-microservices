package com.vromanyu.outbox;

import com.vromanyu.event.OrderCreatedEvent;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "OUTBOX_TABLE", schema = "QUARKUS_OUTBOX")
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ORDER_ID", nullable = false)
    Long orderId;

    @Convert(converter = JsonPayloadConverter.class)
    @Column(name = "PAYLOAD", nullable = false, length = 500)
    OrderCreatedEvent payload;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    Instant createdAt;

    @Column(name = "PROCESSED", nullable = false)
    String processed;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderCreatedEvent getPayload() {
        return payload;
    }

    public void setPayload(OrderCreatedEvent payload) {
        this.payload = payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Outbox{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", payload=" + payload +
                ", createdAt=" + createdAt +
                ", processed=" + processed +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
