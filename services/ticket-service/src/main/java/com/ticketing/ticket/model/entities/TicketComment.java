package com.ticketing.ticket.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comments", indexes = {
        @Index(name = "idx_ticket_comment", columnList = "ticket_id, created_at"),
        @Index(name = "idx_tenant_comment", columnList = "tenant_id, ticket_id")
})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private Long authorId;           // Agent or Customer who wrote the comment

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp

    private LocalDateTime createdAt;

    @PrePersist
    protected void enforceTenant() {
        if (tenantId == null && ticket != null) {
            this.tenantId = ticket.getTenantId();
        }
        if (tenantId == null) {
            throw new IllegalStateException("tenantId must be set");
        }
    }
}