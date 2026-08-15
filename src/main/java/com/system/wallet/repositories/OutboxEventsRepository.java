package com.system.wallet.repositories;

import com.system.wallet.models.Outbox_event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventsRepository extends JpaRepository<Outbox_event , Integer> {


    List<Outbox_event> findTop10ByStatusOrderByCreatedAtAsc(String status);
}
