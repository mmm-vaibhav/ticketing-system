package com.ticketing.notification.db.repos;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.notification.entities.KafkaProcessedEvent;
import com.ticketing.notification.entities.ProcessedEventId;

public interface KafkaProcessedEventRepo extends JpaRepository<KafkaProcessedEvent, Serializable>{
	
	boolean existsById(ProcessedEventId id);
}
