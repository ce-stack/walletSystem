package com.system.wallet.repositories;

import com.system.wallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {


    Optional<Wallet> findById(Integer integer);
}
