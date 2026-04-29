package com.ticketing.tenant.kafka.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.tenant.kafka.repo.ProcessedEventRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;


public class NotificationConsumer {
	
	private final ProcessedEventRepository processedEventRepository;	
	private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    public NotificationConsumer(MeterRegistry meterRegistry, ObjectMapper objectMapper, ProcessedEventRepository processedEventRepository) {
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

//    @KafkaListener(topics = "user-events", groupId = "notification-group")
//    public void consume(String message) {
//        System.out.println("📧 Sending email for event: " + message);
//    }
	
//	@KafkaListener(topics = "user-events", groupId = "notification-group")
//	public void consume(String message) {
//
//		try {
//			
//			int attempt = counter.incrementAndGet();
//
//		    
//	        UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
//
//	        System.out.println("Tenant: " + event.getData().getTenantId());
//	        System.out.println("📧 Attempt: " + attempt);
//	        // 🔥 FORCE FAILURE
//	        throw new RuntimeException("Simulated failure");
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	}
	
//	@KafkaListener(topics = "user-events", groupId = "notification-group")
//	public void consume(String message) {
//		try {
//			UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
//			String eventId = event.getEventId();
//			if (processedEventRepository.existsById(eventId)) {
//				  System.out.println("⚠️ Duplicate event ignored: " + eventId);
//		          return;
//			}
//			if (event.getData().getEmail().contains("2")) {
//			    throw new RuntimeException("🔥 Intentionally failing for testing");
//			}
//			ProcessedEvent processed = new ProcessedEvent();
//			processed.setEventId(eventId);
//			processed.setProcessedAt(LocalDateTime.now());
//			
//			successCounter.increment();
//	        // ✅ PROCESS LOGIC
//	        System.out.println("✅ Processing event: " + eventId);
//			processedEventRepository.save(processed);
//		} catch (Exception e) {
//			failureCounter.increment();
////			retryCounter.increment();
//	        throw new RuntimeException(e);
//	    }
//	}
}