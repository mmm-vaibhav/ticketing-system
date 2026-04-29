package com.ticketing.tenant.service;

import java.util.List;

import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.ui.dto.requests.UserRequestDTO;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;

public interface UserService {
	List<User> getUsersByTenant(Long tenantId);

	UserResponseDTO createUser(UserRequestDTO dto);
}
