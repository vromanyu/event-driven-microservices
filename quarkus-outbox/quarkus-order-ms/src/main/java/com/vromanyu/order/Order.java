package com.vromanyu.order;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "order", schema = "quarkus_outbox")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "order_uuid", nullable = false)
    String orderUuid;

    @Column(name = "item_name", nullable = false, length = 300)
    String itemName;

    @Column(name = "product_type", nullable = false, length = 500)
    String productType;

    @Column(name = "price", nullable = false)
    Double price;

    @Column(name = "quantity", nullable = false)
    Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderUuid='" + orderUuid + '\'' +
                ", itemName='" + itemName + '\'' +
                ", productType='" + productType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
