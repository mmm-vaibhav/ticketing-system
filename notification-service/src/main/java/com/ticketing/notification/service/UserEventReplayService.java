package com.ticketing.notification.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ticketing.notification.utils.AppConstants;

@Service
public class UserEventReplayService {
	
	
	private final KafkaTemplate<String, Object> kafkaTemplate;
	
	public UserEventReplayService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
	
	
	public void replay(Object event) {
        kafkaTemplate.send(AppConstants.USER_TOPIC, event);
    }

}
