package com.ticketing.tenant.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ticketing.tenant.context.TenantContext;
import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.db.repos.OutboxRepository;
import com.ticketing.tenant.db.repos.UserRepository;
import com.ticketing.tenant.kafka.events.UserEventFactory;
import com.ticketing.tenant.kafka.outbox.OutboxEventFactory;
import com.ticketing.tenant.kafka.outbox.entities.OutboxEvent;
import com.ticketing.tenant.service.UserService;
import com.ticketing.tenant.ui.dto.requests.UserRequestRecord;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;
import com.ticketing.tenant.ui.mapper.UserMapper;
import com.ticketing.tenant.validation.UserRoleResolver;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final OutboxRepository outboxRepository;

    private final UserRepository userRepository;
		
	private final UserMapper mapper;
	
	private final UserRoleResolver roleResolver;
	
	private final OutboxEventFactory outboxEventFactory;
	
	private final UserEventFactory userEventFactory;

	
	@Override
	public List<User> getUsersByTenant(Long tenantId) {
		return userRepository.findByTenantId(tenantId);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public UserResponseDTO createUser(UserRequestRecord dto) {
		Long tenantId = TenantContext.getTenant();
	    User user = new User();
	    user.setUserKey(UUID.randomUUID().toString());
	    user.setTenantId(tenantId);
	    user.setEmail(dto.email());
	    user.setPhoneNumber(dto.phoneNumber());
	    user.setRole(roleResolver.getRole(dto.role()));
	    User savedUser = userRepository.save(user);
	    
	    OutboxEvent outboxEvent = outboxEventFactory.createUserCreatedEvent(
                savedUser,
                userEventFactory.createUserCreatedEvent(savedUser, dto.phoneNumber())
        );
	    outboxRepository.save(outboxEvent);
	    return mapper.toResponse(savedUser);
	}
}
