package com.ticketing.tenant.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.tenant.db.entities.Tenant;
import com.ticketing.tenant.db.repos.TenantRepository;
import com.ticketing.tenant.service.TenantService;
import com.ticketing.tenant.ui.dto.requests.TenantRequestDTO;
import com.ticketing.tenant.ui.dto.responses.TenantResponseDTO;

@Service
public class TenantServiceImpl implements TenantService{
	
	@Autowired
	private TenantRepository tenantRepository;
	
	public TenantResponseDTO createTenant(TenantRequestDTO dto) {

	    Tenant tenant = new Tenant();
	    tenant.setName(dto.getName());
	    tenant.setStatus(dto.getStatus());

	    tenant.setTenantKey(UUID.randomUUID().toString());

	    Tenant saved = tenantRepository.save(tenant);

	    TenantResponseDTO response = new TenantResponseDTO();
	    response.setTenantKey(saved.getTenantKey());
	    response.setName(saved.getName());
	    response.setStatus(saved.getStatus());

	    return response;
	}

}
