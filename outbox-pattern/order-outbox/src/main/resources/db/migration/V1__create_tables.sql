CREATE TABLE "outbox"
(
    id         BIGSERIAL PRIMARY KEY,
    event_id   VARCHAR(255)  NOT NULL,
    event_type VARCHAR(255)  NOT NULL,
    payload    VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE "order"
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT                          NOT NULL,
    amount     DOUBLE PRECISION                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    outbox_id  BIGINT REFERENCES "outbox" (id) NOT NULL
);