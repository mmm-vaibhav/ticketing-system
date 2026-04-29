package com.ticketing.tenant.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.db.repos.UserRepository;
import com.ticketing.tenant.kafka.producer.KafkaProducerService;
import com.ticketing.tenant.service.UserService;
import com.ticketing.tenant.ui.dto.requests.UserRequestRecord;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;
import com.ticketing.tenant.utils.AppConstants;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Override
	public List<User> getUsersByTenant(Long tenantId) {
		return userRepository.findByTenantId(tenantId);
	}

	public UserResponseDTO createUser(UserRequestRecord dto) {

	    User user = new User();
	    user.setUserKey(UUID.randomUUID().toString());
	    user.setTenantId(dto.tenantId());
	    user.setEmail(dto.email());
	    user.setPhoneNumber(dto.phoneNumber());
	    user.setRole(dto.role());

	    User savedUser = userRepository.save(user);

	    UserCreatedEvent data = new UserCreatedEvent();
	    data.setUserKey(savedUser.getUserKey());
	    data.setTenantId(savedUser.getTenantId());
	    data.setEmail(savedUser.getEmail());
	    data.setPhoneNumber(savedUser.getPhoneNumber());
	    
	    BaseEvent<UserCreatedEvent> event = new BaseEvent<>();
	    event.setEventId(UUID.randomUUID().toString());
	    event.setEventType(AppConstants.USER_CREATION_EVENT_TYPE);
	    event.setVersion(AppConstants.getEventversion(dto.phoneNumber()));
	    event.setTimestamp(LocalDateTime.now());
	    event.setData(data);	
	    
	    kafkaProducerService.sendUserCreatedEvent(event);
	    return mapToResponse(savedUser);
	}

	private UserResponseDTO mapToResponse(User saved) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(saved.getId());
		dto.setEmail(saved.getEmail());
		dto.setRole(saved.getEmail());
		return dto;
	}
}
