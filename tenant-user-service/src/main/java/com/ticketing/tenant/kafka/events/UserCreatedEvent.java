package com.ticketing.tenant.kafka.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent {

    private String userKey;
    private Long tenantId;
    private String email;

}