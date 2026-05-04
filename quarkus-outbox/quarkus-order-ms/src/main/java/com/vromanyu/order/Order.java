package com.vromanyu.order;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ORDER_TABLE", schema = "QUARKUS_OUTBOX")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ORDER_UUID", nullable = false)
    String orderUuid;

    @Column(name = "ITEM_NAME", nullable = false, length = 300)
    String itemName;

    @Column(name = "PRODUCT_TYPE", nullable = false, length = 500)
    String productType;

    @Column(name = "PRICE", nullable = false)
    BigDecimal price;

    @Column(name = "QUANTITY", nullable = false)
    Integer quantity;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false)
    Instant createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    Instant updatedAt;

    @Override
    public String toString() {
        return (
                "Order{" +
                        "id=" +
                        id +
                        ", orderUuid='" +
                        orderUuid +
                        '\'' +
                        ", itemName='" +
                        itemName +
                        '\'' +
                        ", productType='" +
                        productType +
                        '\'' +
                        ", price=" +
                        price +
                        ", quantity=" +
                        quantity +
                        ", createdAt=" +
                        createdAt +
                        ", updatedAt=" +
                        updatedAt +
                        '}'
        );
    }
}
