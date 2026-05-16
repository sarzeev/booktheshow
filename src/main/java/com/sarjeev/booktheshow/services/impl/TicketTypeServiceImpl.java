package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.exceptions.BookTheShowException;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.exceptions.TicketTypeNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.repositories.TicketTypeRepository;
import com.sarjeev.booktheshow.requests.CreateTicketTypeRequest;
import com.sarjeev.booktheshow.requests.UpdateTicketTypeRequest;
import com.sarjeev.booktheshow.services.TicketTypeService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import com.sarjeev.booktheshow.validators.OwnershipValidator;
import com.sarjeev.booktheshow.validators.TicketTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final OwnershipValidator ownershipValidator;
    private final TicketTypeValidator ticketTypeValidator;

    @Override
    @Transactional
    public TicketType createTicketType(UUID eventId, CreateTicketTypeRequest request) {
        Event event = loadOwnedEvent(eventId);
        ticketTypeValidator.validate(request.name(), request.price(), request.totalAvailable());
        TicketType ticketType = TicketType.builder()
                .name(request.name().trim().toUpperCase())
                .description(request.description())
                .price(request.price())
                .totalAvailable(request.totalAvailable())
                .remainingTickets(request.totalAvailable())
                .event(event)
                .build();
        return ticketTypeRepository.save(ticketType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketType> listTicketTypes(UUID eventId, Pageable pageable) {
        loadOwnedEvent(eventId);
        return ticketTypeRepository.findByEventId(eventId, pageable);
    }

    @Override
    @Transactional
    public TicketType updateTicketType(UUID eventId, UUID ticketTypeId, UpdateTicketTypeRequest request) {
        loadOwnedEvent(eventId);
        TicketType ticketType = ticketTypeRepository.findByIdAndEventId(ticketTypeId, eventId)
                .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type not found for this event"));

        ticketTypeValidator.validate(request.name(), request.price(), request.totalAvailable());
        int soldTickets = ticketType.getTotalAvailable() - ticketType.getRemainingTickets();
        if (request.totalAvailable() < soldTickets) {
            throw new BookTheShowException("Total availability cannot be less than already sold tickets");
        }

        ticketType.setName(request.name().trim().toUpperCase());
        ticketType.setDescription(request.description());
        ticketType.setPrice(request.price());
        ticketType.setTotalAvailable(request.totalAvailable());
        ticketType.setRemainingTickets(request.totalAvailable() - soldTickets);
        return ticketTypeRepository.save(ticketType);
    }

    @Override
    @Transactional
    public void deleteTicketType(UUID eventId, UUID ticketTypeId) {
        loadOwnedEvent(eventId);
        TicketType ticketType = ticketTypeRepository.findByIdAndEventId(ticketTypeId, eventId)
                .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type not found for this event"));
        if (ticketRepository.countByTicketTypeId(ticketTypeId) > 0) {
            throw new BookTheShowException("Ticket types with sold tickets cannot be deleted");
        }
        ticketTypeRepository.delete(ticketType);
    }

    private Event loadOwnedEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        ownershipValidator.validateEventOwnerOrAdmin(event, SecurityUtils.currentUser());
        return event;
    }
}
