package com.ticketing.tenant.context.config;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class TenantFilterConfig {
	
	@PersistenceContext
    private EntityManager entityManager;

    public void enableFilter(Long tenantId) {

        Session session = entityManager.unwrap(Session.class);

        if (session.getEnabledFilter("tenantFilter") == null) {
            session.enableFilter("tenantFilter")
                   .setParameter("tenantId", tenantId);
        }
    }
}
