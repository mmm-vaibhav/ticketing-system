package com.ticketing.notification.context;

public class TenantContext {
	
private static final ThreadLocal<Long> tenantHolder = new ThreadLocal<>();
	
	public static void setTenantId(Long tenantId) {
		tenantHolder.set(tenantId);
	}
	
	public static Long getTenant() {
		Long tenant = tenantHolder.get();
		if (tenant == null) {
			throw new IllegalStateException("Tenant ID is not set");
		}
		return tenant;
	}
	
	
	public static void clear() {
        tenantHolder.remove();
    }

}
