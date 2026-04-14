package com.ticketing.tenant.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ticketing.tenant.service.TenantService;
import com.ticketing.tenant.ui.dto.requests.TenantRequestDTO;
import com.ticketing.tenant.ui.dto.responses.TenantResponseDTO;
import com.ticketing.tenant.utils.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping(AppConstants.BASE_URL + AppConstants.TENANT_API)
public class TenantController {
	
	@Autowired
	private TenantService tenantService;
	
	@PostMapping
	public TenantResponseDTO	 createTenant(@Valid @RequestBody TenantRequestDTO dto) {
	    return tenantService.createTenant(dto);	    		
	}

}
