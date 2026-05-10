package com.ticketing.tenant.kafka.events;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.utils.AppConstants;

@Component
public class UserEventFactory {
	
	

    public BaseEvent<UserCreatedEvent> createUserCreatedEvent(User savedUser, String phoneNumber) {
        UserCreatedEvent data = new UserCreatedEvent();
        data.setUserKey(savedUser.getUserKey());
        data.setEmail(savedUser.getEmail());
        data.setPhoneNumber(savedUser.getPhoneNumber());
        BaseEvent<UserCreatedEvent> event = new BaseEvent<>();
        event.setEventId(UUID.randomUUID().toString());
        event.setTenantId(savedUser.getTenantId());
        event.setEventType(AppConstants.USER_CREATION_EVENT_TYPE);
        event.setVersion(AppConstants.getEventversion(phoneNumber));
        event.setTimestamp(LocalDateTime.now());
        event.setData(data);
        return event;
    }

}
