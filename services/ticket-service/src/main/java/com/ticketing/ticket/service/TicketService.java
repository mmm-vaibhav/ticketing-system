package com.ticketing.ticket.service;

import com.ticketing.ticket.web.dto.requestes.TicketCreateRequest;
import com.ticketing.ticket.web.dto.requestes.TicketUpdateRequest;
import com.ticketing.ticket.web.dto.response.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketResponse createTicket(TicketCreateRequest request);

    TicketResponse updateTicket(Long ticketId, TicketUpdateRequest request);

    TicketResponse getTicket(Long ticketId);

    Page<TicketResponse> getTicketsByTenant(Pageable pageable);

    Page<TicketResponse> getTicketsByStatus(String status, Pageable pageable);

    Page<TicketResponse> getTicketsByPriority(String priority, Pageable pageable);

    void changeStatus(Long ticketId, String newStatus);

    void addComment(Long ticketId, String content);
}
