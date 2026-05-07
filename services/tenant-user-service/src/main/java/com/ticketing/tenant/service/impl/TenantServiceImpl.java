package com.ticketing.tenant.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.tenant.db.entities.Tenant;
import com.ticketing.tenant.db.enums.TenantStatus;
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
	    tenant.setStatus(TenantStatus.ACTIVE);
	    tenant.setTenantKey(getTenantKey(dto.getName()));
	    Tenant saved = tenantRepository.save(tenant);
	    return TenantResponseDTO.builder()
	    .tenantKey(saved.getTenantKey())
	    .name(saved.getName())
	    .status(saved.getStatus()).build();
	}

	private String getTenantKey(String name) {
        String processed = name;
        
        // Optional: Special handling for company names
        if (name.contains(" - ")) {
            processed = name.replace(" - ", "-");
        }
        
        return processed.toLowerCase()
                       .replaceAll("[\\s\\-_]+", "_")
                       .replaceAll("[^a-z0-9_]", "");
    }

}
