package com.system.wallet.repositories;

import com.system.wallet.models.Idempotency_key;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<Idempotency_key, Integer> {

    boolean existsByKey(String key);
}
