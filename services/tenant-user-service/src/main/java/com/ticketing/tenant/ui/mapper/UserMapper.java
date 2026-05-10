package com.ticketing.tenant.ui.mapper;

import org.springframework.stereotype.Component;

import com.ticketing.tenant.db.entities.User;
import com.ticketing.tenant.ui.dto.responses.UserResponseDTO;

@Component
public class UserMapper {
	
	  public UserResponseDTO toResponse(User saved) {
	        UserResponseDTO dto = new UserResponseDTO();
	        dto.setId(saved.getId());
	        dto.setEmail(saved.getEmail());
	        dto.setRole(saved.getRole().name());
	        return dto;
	    }
}
