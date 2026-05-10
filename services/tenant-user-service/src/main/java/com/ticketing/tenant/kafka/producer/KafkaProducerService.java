package com.ticketing.tenant.kafka.producer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ticketing.tenant.db.repos.OutboxRepository;
import com.ticketing.tenant.kafka.events.OutboxStatus;
import com.ticketing.tenant.kafka.outbox.OutboxEventFactory;
import com.ticketing.tenant.kafka.outbox.entities.OutboxEvent;
import com.ticketing.tenant.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

//    @Autowired
//    private KafkaTemplate<String, BaseEvent<UserCreatedEvent>> kafkaTemplate;
//    
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    public void sendUserCreatedEvent(BaseEvent<UserCreatedEvent> event) {
//        try {
//            kafkaTemplate.send(AppConstants.USER_TOPIC, event);
//        } catch (Exception e) {
//            throw new RuntimeException("Error sending event");
//        }
//    }
	
	
	
	private final OutboxEventFactory outboxEventFactory;
	private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publish() {
//        log.info();
    	System.out.println("Outbox events publishing...");
        List<OutboxEvent> events = outboxRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
        for (OutboxEvent outboxEvent : events) {
            try {
//                log.info("Publishing Outbox Event: {}", outboxEvent.getId());
            	System.out.println("Publishing Outbox Event: " + outboxEvent.getId());
                // Send synchronously
                kafkaTemplate.send(AppConstants.USER_TOPIC, outboxEvent.getPayload()).get();

                // Mark sent
                outboxEvent.setStatus(OutboxStatus.SENT);
                outboxEvent.setUpdatedAt(LocalDateTime.now());

                // ✅ Persist changes after success
                outboxRepository.save(outboxEvent);

            } catch (Exception e) {
//                log.error("Event send failed: {}", outboxEvent.getId(), e);
            	System.out.println("Event send failed: " + outboxEvent.getId());
                outboxEvent.setRetryCount(outboxEvent.getRetryCount() + 1);
                if (outboxEvent.getRetryCount() > 5) {
                    outboxEvent.setStatus(OutboxStatus.FAILED); // Use DEAD instead of FAILED for clarity
//                    log.error("Event moved to DEAD: {}", outboxEvent.getId());
                    System.out.println("Event moved to DEAD:" + outboxEvent.getId());
                } else {
                    outboxEvent.setStatus(OutboxStatus.RETRYABLE_FAILED); // distinguish retryable
                }

                outboxEvent.setUpdatedAt(LocalDateTime.now());
                outboxRepository.save(outboxEvent);
            }
        }
    }
}