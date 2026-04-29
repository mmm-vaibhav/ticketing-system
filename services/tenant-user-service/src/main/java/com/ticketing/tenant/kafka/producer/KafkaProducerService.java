package com.ticketing.tenant.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.tenant.kafka.dtos.UserCreatedEvent;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, BaseEvent<UserCreatedEvent>> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOPIC = "user-events";

    public void sendUserCreatedEvent(BaseEvent<UserCreatedEvent> event) {

        try {
//        	event.setEventId("ed4325fd-8ae7-4c9c-a2b6-67895c54640c");
//            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event);
        } catch (Exception e) {
            throw new RuntimeException("Error sending event");
        }
    }
}