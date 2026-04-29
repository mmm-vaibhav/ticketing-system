package com.ticketing.notification.service.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.notification.service.UserEventReplayService;

@Service
public class DLTConsumer {
	
	private final UserEventReplayService userEventReplayService;
	
	private final ObjectMapper objectMapper;
	
	public DLTConsumer(UserEventReplayService userEventReplayService, ObjectMapper objectMapper) {
		this.userEventReplayService = userEventReplayService;
		this.objectMapper = objectMapper;
	}


	@KafkaListener(topics = "user-events-dlt", groupId = "replay-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeFromDLT(String message) {

        
        
        try {
            BaseEvent event = objectMapper.readValue(message, BaseEvent.class);
            System.out.println("📦 Received from DLT: " + event.getEventId());
            event.setReplayed(true);
            System.out.println("📦 DLT event: " + event.getEventId());

        } catch (Exception e) {
            System.out.println("❌ Failed to parse DLT message");
        }

        

        // replay back to main topic
//        userEventReplayService.replay(event);
    }
	
	
//	public void replayEvent(BaseEvent event) {
//		userEventReplayService.replay(event);
//	}

}
