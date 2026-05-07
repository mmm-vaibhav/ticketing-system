package com.ticketing.tenant.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketing.common.events.BaseEvent;
import com.ticketing.common.events.UserCreatedEvent;
import com.ticketing.tenant.context.TenantContext;
import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.db.enums.UserRoles;
import com.ticketing.tenant.db.repos.OutboxRepository;
import com.ticketing.tenant.db.repos.UserRepository;
import com.ticketing.tenant.kafka.events.OutboxStatus;
import com.ticketing.tenant.kafka.outbox.entities.OutboxEvent;
import com.ticketing.tenant.service.UserService;
import com.ticketing.tenant.ui.dto.requests.UserRequestRecord;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;
import com.ticketing.tenant.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final OutboxRepository outboxRepository;

    private final UserRepository userRepository;
	
	private final ObjectMapper objectMapper;

	

	@Override
	public List<User> getUsersByTenant(Long tenantId) {
		return userRepository.findByTenantId(tenantId);
	}

	public UserResponseDTO createUser(UserRequestRecord dto) {
		Long tenantId = TenantContext.getTenant();
	    User user = new User();
	    user.setUserKey(UUID.randomUUID().toString());
	    user.setTenantId(tenantId);
	    user.setEmail(dto.email());
	    user.setPhoneNumber(dto.phoneNumber());
	    user.setRole(getRole(dto.role()));
	    User savedUser = userRepository.save(user);
	    
	    //eventCreation
	    UserCreatedEvent data = new UserCreatedEvent();
	    data.setUserKey(savedUser.getUserKey());
	    data.setEmail(savedUser.getEmail());
	    data.setPhoneNumber(savedUser.getPhoneNumber());
	    
	    BaseEvent<UserCreatedEvent> event = new BaseEvent<>();
	    event.setEventId(UUID.randomUUID().toString());
	    event.setTenantId(savedUser.getTenantId());
	    event.setEventType(AppConstants.USER_CREATION_EVENT_TYPE);
	    event.setVersion(AppConstants.getEventversion(dto.phoneNumber()));
	    event.setTimestamp(LocalDateTime.now());
	    event.setData(data);
	    
	    OutboxEvent outEvent = new OutboxEvent();
	    try {
	    	outEvent.setId(event.getEventId());
		    outEvent.setTenantId(tenantId);
		    outEvent.setEventType(event.getEventType());
			outEvent.setPayload(objectMapper.writeValueAsString(event));
			outEvent.setStatus(OutboxStatus.PENDING);
			outEvent.setCreatedAt(LocalDateTime.now());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    outboxRepository.save(outEvent);
	    return mapToResponse(savedUser);
	}

	private UserRoles getRole(String role) {
		if (role == null || role.trim().isEmpty()) {
			throw new IllegalArgumentException("Role cannot be null or empty");
		}
		return Arrays.stream(UserRoles.values())
				.filter(userrole -> userrole.name().contains(role))
				.findFirst().orElseThrow(() -> new IllegalArgumentException	("No role found containing: " + role));
	}

	private UserResponseDTO mapToResponse(User saved) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(saved.getId());
		dto.setEmail(saved.getEmail());
		dto.setRole(saved.getRole().name().toString());
		return dto;
	}
}
