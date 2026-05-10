package com.ticketing.tenant.kafka.outbox.entities;

import java.time.LocalDateTime;

import com.ticketing.tenant.kafka.events.OutboxStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "outbox_events", 
indexes = @Index(name = "idx_status_created", columnList = "status, createdAt"))

@Getter
@Setter
@ToString
public class OutboxEvent {

    @Id
    private String id;

    private Long tenantId;

    private String eventType;

    @Lob
    private String payload;
    
    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, SENT, FAILED

    private int retryCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}