package com.ticketing.tenant.ui.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantResponseDTO {

	private String tenantKey;
	private String name;
	private String status;
}
