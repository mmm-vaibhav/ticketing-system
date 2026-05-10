package com.ticketing.notification.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

@Embeddable
@AllArgsConstructor
public class ProcessedEventId implements Serializable {
    private static final long serialVersionUID = 1L;
    
	private String eventId;
    private Long tenantId;
}