package com.ticketing.tenant.kafka.consumers;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;


public class KafkaConsumerService {

	
	@Autowired
	private ObjectMapper objectMapper;
	
//	@KafkaListener(topics = "user-events", groupId = "ticketing-group")
//	public void consume(String message) {
//
//	    try {
//	        UserCreatedEvent event = objectMapper.readValue(message, UserCreatedEvent.class);
//	        System.out.println("🔥 User created for tenant: " + event.getData().getTenantId());
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	}
}