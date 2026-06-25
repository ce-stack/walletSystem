CREATE TABLE wallets (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL ,
    balance DECIMAL NOT NULL ,
    currency VARCHAR(255) NOT NULL ,
    status VARCHAR(255) NOT NULL ,
    version INT NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key (id) ,
    CONSTRAINT fk_wallets_user
                     FOREIGN KEY (user_id)
                     REFERENCES users(id)
);