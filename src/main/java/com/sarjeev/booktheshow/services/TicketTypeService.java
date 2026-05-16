package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.requests.CreateTicketTypeRequest;
import com.sarjeev.booktheshow.requests.UpdateTicketTypeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketTypeService {

    TicketType createTicketType(UUID eventId, CreateTicketTypeRequest request);

    Page<TicketType> listTicketTypes(UUID eventId, Pageable pageable);

    TicketType updateTicketType(UUID eventId, UUID ticketTypeId, UpdateTicketTypeRequest request);

    void deleteTicketType(UUID eventId, UUID ticketTypeId);
}
