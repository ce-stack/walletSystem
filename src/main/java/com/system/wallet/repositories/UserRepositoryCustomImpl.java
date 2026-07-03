package com.system.wallet.repositories;

import com.system.wallet.models.Transaction;
import com.system.wallet.models.Wallet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {



    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void creteWallet(Wallet wallet) {
        entityManager.persist(wallet);
    }

}
