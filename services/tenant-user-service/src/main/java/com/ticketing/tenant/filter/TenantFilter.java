package com.ticketing.tenant.filter;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.ticketing.tenant.context.TenantContext;
import com.ticketing.tenant.utils.AppConstant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {
	
    private final HandlerExceptionResolver resolver;
	
	public TenantFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String servletPath = request.getServletPath();
		return servletPath.startsWith("/actuator");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {

            String tenantHeader = request.getHeader(AppConstant.TENANT_HEADER);

            if (tenantHeader == null || tenantHeader.isBlank()) {
                throw new IllegalArgumentException("X-Tenant-Id header is missing");
            }

            Long tenantId = Long.valueOf(tenantHeader);
            
            if (tenantId <= 0) {
                throw new IllegalArgumentException("X-Tenant-Id Must be positive value.");
            }

            TenantContext.setTenantId(tenantId);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {

            resolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        } finally {
            TenantContext.clear();
        }
	}
}
