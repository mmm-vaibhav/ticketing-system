package com.ticketing.tenant.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.db.repos.UserRepository;
import com.ticketing.tenant.kafka.producer.KafkaProducerService;
import com.ticketing.tenant.service.UserService;
import com.ticketing.tenant.ui.dto.requests.UserRequestDTO;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;

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

	public UserResponseDTO createUser(UserRequestDTO dto) {

	    User user = new User();
	    user.setTenantId(dto.getTenantId());
	    user.setEmail(dto.getEmail());
	    user.setUserKey(UUID.randomUUID().toString());

	    User saved = userRepository.save(user);

	    // 🔥 Publish event
	    kafkaProducerService.sendUserCreatedEvent(
	        "User created: " + saved.getUserKey()
	    );

	    return mapToResponse(saved);
	}

	private UserResponseDTO mapToResponse(User saved) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(saved.getId());
		dto.setEmail(saved.getEmail());
		dto.setRole(saved.getEmail());
		return dto;
	}

}
