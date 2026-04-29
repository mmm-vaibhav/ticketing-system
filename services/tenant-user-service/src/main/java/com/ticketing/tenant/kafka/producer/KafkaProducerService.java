package com.ticketing.tenant.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.tenant.utils.AppConstants;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, BaseEvent<UserCreatedEvent>> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;


    public void sendUserCreatedEvent(BaseEvent<UserCreatedEvent> event) {
        try {
            kafkaTemplate.send(AppConstants.USER_TOPIC, event);
        } catch (Exception e) {
            throw new RuntimeException("Error sending event");
        }
    }
}