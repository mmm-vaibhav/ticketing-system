package com.ticketing.tenant.kafka.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent {

    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;

    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String userKey;
        private Long tenantId;
        private String email;
    }
}