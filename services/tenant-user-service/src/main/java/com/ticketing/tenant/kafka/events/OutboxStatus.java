package com.ticketing.tenant.kafka.events;

public enum OutboxStatus {
    PENDING,
    SENT,
    FAILED
}