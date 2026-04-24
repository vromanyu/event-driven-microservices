package com.vromanyu.outbox;

import com.vromanyu.event.OrderCreatedEvent;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "outbox", schema = "quarkus_outbox")
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Convert(converter = JsonPayloadConverter.class)
    @Column(name = "payload", nullable = false, length = 500)
    OrderCreatedEvent payload;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Instant createdAt;

    @Column(name = "processed", nullable = false)
    Boolean processed;

    @UpdateTimestamp
    @Column(name = "updated_at")
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

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
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
