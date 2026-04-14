package com.ticketing.tenant.db.repos;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.tenant.db.entities.User;

public interface UserRepository extends JpaRepository<User, Serializable> {
	
	List<User> findByTenantId(Long tenantId);

}
