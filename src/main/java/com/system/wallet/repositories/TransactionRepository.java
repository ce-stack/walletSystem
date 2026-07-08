package com.system.wallet.repositories;

import com.system.wallet.config.enums.TransactionStatus;
import com.system.wallet.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.from_wallet_id.id = :fromWalletId
        AND t.to_wallet_id.id = :toWalletId
        AND t.status = :status    
        ORDER BY t.id DESC
    """)
    List<Transaction> findLatestTransactions(
            @Param("fromWalletId") Integer fromWalletId,
            @Param("toWalletId") Integer toWalletId,
            @Param("status") TransactionStatus status,
            Pageable pageable
    );
}
