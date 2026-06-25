CREATE TABLE ledger_entries(
    id INT NOT NULL AUTO_INCREMENT,
    transaction_id INT NOT NULL ,
    wallet_id INT NOT NULL ,
    types VARCHAR(255) NOT NULL ,
    amount DECIMAL(15,2) NOT NULL ,
    balance_after DECIMAL(15,2) NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (id),
    CONSTRAINT fk_ledger_entries_transaction
                           FOREIGN KEY (transaction_id)
                           REFERENCES transactions(id),

    CONSTRAINT fk_ledger_entries_wallet
                           FOREIGN KEY (wallet_id)
                           REFERENCES wallets(id)
);