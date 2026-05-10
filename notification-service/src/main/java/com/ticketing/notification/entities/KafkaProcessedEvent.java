	package com.ticketing.notification.entities;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
public class KafkaProcessedEvent {
	
	@EmbeddedId
    private ProcessedEventId id;

    private LocalDateTime processedAt;

}
