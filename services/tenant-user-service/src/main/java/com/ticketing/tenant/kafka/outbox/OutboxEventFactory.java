package com.ticketing.tenant.kafka.outbox;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.kafka.events.OutboxStatus;
import com.ticketing.tenant.kafka.outbox.entities.OutboxEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxEventFactory {
	
	  private final ObjectMapper objectMapper;

	    public OutboxEvent createUserCreatedEvent(User savedUser, BaseEvent<UserCreatedEvent> event) {
	        try {
	            OutboxEvent outboxEvent = new OutboxEvent();
	            outboxEvent.setId(event.getEventId());
	            outboxEvent.setTenantId(savedUser.getTenantId());
	            outboxEvent.setEventType(event.getEventType());
	            outboxEvent.setPayload(objectMapper.writeValueAsString(event));
	            outboxEvent.setStatus(OutboxStatus.PENDING);
	            outboxEvent.setCreatedAt(LocalDateTime.now());
	            return outboxEvent;
	        } catch (JsonProcessingException ex) {
	            throw new IllegalStateException("Failed to serialize outbox event", ex);
	        }
	    }

}
