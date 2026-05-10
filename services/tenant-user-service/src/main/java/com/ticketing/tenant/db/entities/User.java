	package com.ticketing.tenant.db.entities;
	
	import org.hibernate.annotations.Filter;
	import org.hibernate.annotations.FilterDef;
	import org.hibernate.annotations.ParamDef;
	
	import com.ticketing.tenant.db.enums.UserRoles;
	
	import jakarta.persistence.Entity;
	import jakarta.persistence.EnumType;
	import jakarta.persistence.Enumerated;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.Table;
	import lombok.Getter;
	import lombok.Setter;
	
	@Entity
	@Table(name = "users")
	@FilterDef(name = "tenantFilter",
	parameters = @ParamDef(name = "tenantId", type = Long.class))
	@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
	@Getter
	@Setter
	public class User {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    private Long tenantId;
	    private String userKey;
	    private String email;
	    private String phoneNumber;
	    
	    @Enumerated(EnumType.STRING)
	    private UserRoles role;
	}