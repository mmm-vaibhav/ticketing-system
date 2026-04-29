package com.ticketing.tenant.kafka.consumers;

import org.springframework.kafka.annotation.KafkaListener;


public class AnalyticsConsumer {

    @KafkaListener(topics = "user-events", groupId = "analytics-group")
    public void consume(String message) {
        System.out.println("📊 Analytics updated: " + message);
    }
}