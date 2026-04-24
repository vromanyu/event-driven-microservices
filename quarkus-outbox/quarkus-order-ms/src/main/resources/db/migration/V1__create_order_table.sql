CREATE TABLE quarkus_outbox."order"
(
    id           BIGSERIAL PRIMARY KEY,
    item_name    VARCHAR(300)                            NOT NULL,
    product_type VARCHAR(500)                            NOT NULL,
    quantity     INTEGER                                 NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE
);