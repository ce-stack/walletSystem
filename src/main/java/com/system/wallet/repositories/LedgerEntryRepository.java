package com.system.wallet.repositories;

import com.system.wallet.models.Ledger_entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerEntryRepository extends JpaRepository<Ledger_entry , Integer>{
}
