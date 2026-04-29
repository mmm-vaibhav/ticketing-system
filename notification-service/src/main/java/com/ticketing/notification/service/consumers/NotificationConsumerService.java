package com.ticketing.notification.service.consumers;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.notification.db.repos.KafkaProcessedEventRepo;
import com.ticketing.notification.entities.KafkaProcessedEvent;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@Service
public class NotificationConsumerService {
	
	
	private final KafkaProcessedEventRepo processedEventRepository;	
	private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    public NotificationConsumerService(MeterRegistry meterRegistry, ObjectMapper objectMapper, KafkaProcessedEventRepo processedEventRepository) {
        this.meterRegistry = meterRegistry;
        this.objectMapper = objectMapper;
        this.processedEventRepository = processedEventRepository;
    }
    
    
    private Counter successCounter;
    private Counter failureCounter;
    private Counter retryCounter;

    @PostConstruct
    public void init() {
        successCounter = meterRegistry.counter("events_processed_total");
        failureCounter = meterRegistry.counter("events_failed_total");
        retryCounter = meterRegistry.counter("events_retried_total");
    }

	
	@KafkaListener(topics = "user-events", groupId = "notification-group-v2")
	public void consume(BaseEvent<UserCreatedEvent> event) {
		try {
			System.out.println("Notofication consumer from tenanet service..\n" + event.toString());
//			UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
//			BaseEvent<UserCreatedEvent> event =
//					objectMapper.readValue(message,
//			            new TypeReference<BaseEvent<UserCreatedEvent>>() {});
			String eventId = event.getEventId();
			if (processedEventRepository.existsById(eventId)) {
				  System.out.println("⚠️ Duplicate event ignored: " + eventId);
		          return;
			}
			UserCreatedEvent userEvent = event.getData();
//			if (userEvent.getData().getEmail().contains("2")) {
//			    throw new RuntimeException("🔥 Intentionally failing for testing");
//			}
			KafkaProcessedEvent processed = new KafkaProcessedEvent();
			
			processed.setEventId(eventId);
			processed.setProcessedAt(LocalDateTime.now());
			
			successCounter.increment();
	        // ✅ PROCESS LOGIC
	        System.out.println("✅ Processing event: " + eventId);
			processedEventRepository.save(processed);
		} catch (Exception e) {
			failureCounter.increment();
//			retryCounter.increment();
	        throw new RuntimeException(e);
	    }
	}

}
