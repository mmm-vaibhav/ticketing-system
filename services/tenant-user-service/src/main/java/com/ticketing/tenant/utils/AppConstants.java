package com.ticketing.tenant.utils;

import java.util.Optional;

public interface AppConstants {
	
	String BASE_URL = "/tickets/api/v1";
	String USER_API="/users";
	String TENANT_API="/tenants";
	String USER_TOPIC="user-events";
	String USER_CREATION_EVENT_TYPE="USER_CREATED";
	String USER_CREATED_V1 = "v1";
	String USER_CREATED_V2 = "v2";
	
	static String getEventversion(String phoneNumber) {
		return Optional.ofNullable(phoneNumber)
				.filter(pn -> !pn.trim().isEmpty())
				.map(pn -> USER_CREATED_V2).orElse(USER_CREATED_V1);
	}

}
