package com.ticketing.tenant.ui.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequestRecord(
		 	@NotNull
		    Long tenantId,
		    @Email
		    String email,
		    String phoneNumber,
		    String role
		) {

}
