package com.ticketing.tenant.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.tenant.kafka.events.OutboxStatus;
import com.ticketing.tenant.kafka.outbox.entities.OutboxEvent;

public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}
