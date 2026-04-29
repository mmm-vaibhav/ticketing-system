package com.ticketing.common.events;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
public class UserCreatedEvent {
	
    private String eventId;
    private String eventType;
    private LocalDateTime timestamp;

    private UserData data;

    @Getter
    @Setter
    public static class UserData {
        private String userKey;
        private Long tenantId;
        private String email;
    }

}
