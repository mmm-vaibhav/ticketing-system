package com.ticketing.tenant.validation;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.ticketing.tenant.db.enums.UserRoles;

@Component
public class UserRoleResolver {
	
	
	public UserRoles getRole(String role) {
		if (role == null || role.trim().isEmpty()) {
			throw new IllegalArgumentException("Role cannot be null or empty");
		}
		return Arrays.stream(UserRoles.values())
				.filter(userrole -> userrole.name().contains(role))
				.findFirst().orElseThrow(() -> new IllegalArgumentException	("No role found containing: " + role));
	}

}
