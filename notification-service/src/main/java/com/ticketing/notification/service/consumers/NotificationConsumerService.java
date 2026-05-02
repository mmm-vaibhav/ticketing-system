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
    public void consume(BaseEvent event) {

        String eventId = event.getEventId();

        if (processedEventRepository.existsById(eventId)) {
            return;
        }
        
        UserCreatedEvent user = objectMapper.convertValue(event.getData(), UserCreatedEvent.class);
        
        if (user.getEmail().contains("2") && !event.isReplayed()) {
        	System.out.println("User is having 2 and it is a fresh event having '2' in email so we are retrying..");
        	throw new RuntimeException("Intentionally failing for testing..");
        }

        System.out.println("✅ Processing: Event \n" + event);
        
        if (event.isReplayed()) {
            System.out.println("🔁 Processing replayed event in notification consumer..");
            System.out.println("🔁 Processing replayed event in notification consumer.. and email is: " + user.getEmail());
        }
        
        KafkaProcessedEvent processed = new KafkaProcessedEvent();
        processed.setEventId(eventId);
        processed.setProcessedAt(LocalDateTime.now());
        processedEventRepository.save(processed);
        successCounter.increment();
    }

}
