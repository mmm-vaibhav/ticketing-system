package com.ticketing.ticket.service.impl;

import com.ticketing.ticket.repositories.TicketRepository;
import com.ticketing.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketOutBoxPublisher ticketOutBoxPublisher;



    

}
