package com.ticketing.tenant.ui.dto.responses;

import com.ticketing.tenant.db.enums.TenantStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantResponseDTO {

	private String tenantKey;
	private String name;
	private TenantStatus status;
}
