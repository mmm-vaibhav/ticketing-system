package com.ticketing.common.events;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaseEvent<T> {
    private String eventId;
    private String eventType;
    private String version;
    private boolean replayed;
    private LocalDateTime timestamp;
    private T data;
}
