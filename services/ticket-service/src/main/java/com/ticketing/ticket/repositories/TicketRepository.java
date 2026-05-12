package com.ticketing.ticket.repositories;

import com.ticketing.ticket.model.domain.TicketStatus;
import com.ticketing.ticket.model.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @EntityGraph(attributePaths = {"comments"})
    Optional<Ticket> findByIdAndTenantId(Long id, Long tenantId);

    Page<Ticket> findByTenantId(Long tenantId, Pageable pageable);

    Page<Ticket> findByTenantIdAndStatus(Long tenantId, TicketStatus status, Pageable pageable);

    Page<Ticket> findByTenantIdAndPriorityOrderByCreatedAtDesc(Long tenantId, String priority, Pageable pageable);

    Page<Ticket> findByTenantIdAndAssigneeId(Long tenantId, Long assigneeId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.tenantId = :tenantId AND t.id = :id")
    Optional<Ticket> findByIdWithTenantSafety(@Param("id") Long id, @Param("tenantId") Long tenantId);
}
