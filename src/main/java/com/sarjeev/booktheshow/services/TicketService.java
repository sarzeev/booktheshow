package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketService {

    Page<Ticket> listTicketsForCurrentAttendee(Pageable pageable);

    Ticket getTicketForCurrentAttendee(UUID ticketId);
}
