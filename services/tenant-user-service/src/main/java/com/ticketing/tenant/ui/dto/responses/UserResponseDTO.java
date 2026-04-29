package com.ticketing.tenant.ui.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
	
    private Long id;
    private String email;
    private String role;

}
