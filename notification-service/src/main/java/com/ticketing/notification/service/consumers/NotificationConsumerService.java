package com.ticketing.notification.service.consumers;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.notification.context.TenantContext;
import com.ticketing.notification.db.repos.KafkaProcessedEventRepo;
import com.ticketing.notification.entities.KafkaProcessedEvent;
import com.ticketing.notification.entities.ProcessedEventId;
import com.ticketing.notification.service.NotificationService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationConsumerService {
	
	
	private final KafkaProcessedEventRepo processedEventRepository;	
	private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;
    private final NotificationService notificationService;

//    public NotificationConsumerService(MeterRegistry meterRegistry, ObjectMapper objectMapper, KafkaProcessedEventRepo processedEventRepository) {
//        this.meterRegistry = meterRegistry;
//        this.objectMapper = objectMapper;
//        this.processedEventRepository = processedEventRepository;
//    }
    
    
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

        Long tenantId = Long.valueOf(event.getTenantId());

        if (tenantId == null) {
            throw new RuntimeException("Missing tenantId in event");
        }
        TenantContext.setTenantId(tenantId);
        try {
            String eventId = event.getEventId();
            ProcessedEventId id = new ProcessedEventId(eventId, tenantId);
            // ✅ Tenant-aware idempotency
            if (processedEventRepository.existsById(id)) {
                return;
            }

            UserCreatedEvent user =
                    objectMapper.convertValue(event.getData(), UserCreatedEvent.class);

            if (user.getEmail().contains("2") && !event.isReplayed()) {
                System.out.println("Retrying scenario triggered...");
                throw new RuntimeException("Intentional failure");
            }

            if (event.isReplayed()) {
                System.out.println("🔁 Replay event: " + user.getEmail());
            }
            
            System.out.println("✅ Processing Event: " + event);
            boolean result = notificationService.notifyUser(event.getData().getEmail());
            if (result) {
            	// ✅ Save with tenant_id
                KafkaProcessedEvent processed = new KafkaProcessedEvent();
                processed.setId(id);
                processed.setProcessedAt(LocalDateTime.now());
                processedEventRepository.save(processed);
                successCounter.increment();
            }
        } finally {
            TenantContext.clear(); // 🔥 CRITICAL
        }
    }
}
