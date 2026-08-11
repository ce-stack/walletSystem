package com.system.wallet.repositories;

import com.system.wallet.models.Outbox_event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventsRepository extends JpaRepository<Outbox_event , Integer> {
}
