CREATE TABLE outbox
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT                                  NOT NULL,
    payload    VARCHAR(500)                            NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    processed  BOOLEAN                                 NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP WITHOUT TIME ZONE
);