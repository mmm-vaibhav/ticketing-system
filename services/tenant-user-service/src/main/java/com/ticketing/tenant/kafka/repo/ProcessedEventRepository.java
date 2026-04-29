package com.ticketing.tenant.kafka.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.tenant.kafka.entity.ProcessedEvent;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Serializable>{

}
