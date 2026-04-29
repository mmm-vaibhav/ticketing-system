package com.ticketing.tenant.service;

import com.ticketing.tenant.ui.dto.requests.TenantRequestDTO;
import com.ticketing.tenant.ui.dto.responses.TenantResponseDTO;

public interface TenantService {
	TenantResponseDTO createTenant(TenantRequestDTO tenant);
}
