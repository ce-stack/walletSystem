CREATE TABLE transactions(
    id INT NOT NULL AUTO_INCREMENT,
    from_wallet_id INT NOT NULL ,
    to_wallet_id INT NOT NULL ,
    amount DECIMAL(15,2) NOT NULL ,
    types VARCHAR(255) NOT NULL ,
    status VARCHAR(255) NOT NULL ,
    ref_no INT NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (id),
    CONSTRAINT fk_transactions_wallet
                         foreign key (from_wallet_id)
                         references wallets(id),

    CONSTRAINT fk_transactions_wallet
                         foreign key (to_wallet_id)
                         references wallets(id)
);