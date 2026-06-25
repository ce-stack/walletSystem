CREATE TABLE outbox_events(
    id INT NOT NULL AUTO_INCREMENT,
    idempotency_key_id INT NOT NULL ,
    event_type VARCHAR(255) NOT NULL ,
    payload VARCHAR(255) NOT NULL ,
    status VARCHAR(255) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (id),
    CONSTRAINT fk_outbox_events_idempotency_key
                          FOREIGN KEY (idempotency_key_id)
                          REFERENCES idempotency_keys(id)
);