package com.ticketing.tenant.db.repos;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.tenant.db.entities.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Serializable>{

}
