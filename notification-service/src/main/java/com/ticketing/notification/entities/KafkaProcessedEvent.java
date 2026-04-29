package com.ticketing.notification.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "processed_events")
@Getter
@Setter
public class KafkaProcessedEvent {
	
    @Id
    private String eventId;

    private LocalDateTime processedAt;

}
