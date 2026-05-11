package com.ticketing.ticket.model.entities;

import com.ticketing.ticket.model.domain.TicketStatus;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_tenant_ticket", columnList = "tenant_id, id"),
        @Index(name = "idx_status_assignee", columnList = "tenant_id, status, assignee_id"),
        @Index(name = "idx_created_tenant", columnList = "tenant_id, created_at")
})
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = Long.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 50)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    private Long assigneeId;
    private Long reporterId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

    /** PRIORITY IS STRICTLY STRING AS PER YOUR REQUIREMENT */
    @Column(nullable = false, length = 20)
    private String priority = "MEDIUM";   // Values: LOW, MEDIUM, HIGH, URGENT (validated in service)

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TicketComment> comments = new ArrayList<>();

    @PrePersist
    @PreUpdate
    protected void enforceTenantAndTimestamps() {
        if (tenantId == null) {
            throw new IllegalStateException("tenantId must be set via TenantContext in service layer");
        }
        updatedAt = LocalDateTime.now();
    }

    public void changeStatus(TicketStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + status + " to " + newStatus);
        }
        this.status = newStatus;
        if (newStatus == TicketStatus.RESOLVED) this.resolvedAt = LocalDateTime.now();
        if (newStatus == TicketStatus.CLOSED) this.closedAt = LocalDateTime.now();
    }

    public void addComment(String content, Long authorId) {
        TicketComment comment = new TicketComment();
        comment.setTicket(this);
        comment.setTenantId(this.tenantId);
        comment.setContent(content);
        comment.setAuthorId(authorId);
        this.comments.add(comment);
    }



}
