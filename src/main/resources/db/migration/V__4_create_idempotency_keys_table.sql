CREATE TABLE idempotency_keys(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL ,
    key VARCHAR(255) NOT NULL ,
    request_hash VARCHAR(255) NOT NULL ,
    transaction_id INT NOT NULL ,
    status VARCHAR(255) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (id),
    CONSTRAINT fk_idempotency_keys_user
                             FOREIGN KEY (user_id)
                             REFERENCES users(id),

    CONSTRAINT fk_idempotency_keys_transaction
                             FOREIGN KEY (transaction_id)
                             REFERENCES transactions(id)
);