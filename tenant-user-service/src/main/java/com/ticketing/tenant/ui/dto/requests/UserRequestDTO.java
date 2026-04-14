package com.ticketing.tenant.ui.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
	

    @NotNull
    private Long tenantId;

    @Email
    private String email;

    private String role;

}
