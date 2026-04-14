package com.ticketing.tenant.ui.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantRequestDTO {
	
    @NotBlank
    private String name;

    private String status;
}
