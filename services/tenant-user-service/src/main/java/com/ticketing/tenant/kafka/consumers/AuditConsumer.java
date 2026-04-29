package com.ticketing.tenant.kafka.consumers;

import org.springframework.kafka.annotation.KafkaListener;


public class AuditConsumer {

    @KafkaListener(topics = "user-events", groupId = "audit-group")
    public void consume(String message) {
        System.out.println("🧾 Audit log stored: " + message);
    }
}