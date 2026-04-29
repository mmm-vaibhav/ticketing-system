package com.ticketing.common.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserCreatedEvent {
	
    	private String userKey;
        private Long tenantId;
        private String email;
        private String phoneNumber;

}
