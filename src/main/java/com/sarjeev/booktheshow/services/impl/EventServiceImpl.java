package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.enums.EventStatusEnum;
import com.sarjeev.booktheshow.exceptions.BookTheShowException;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.exceptions.TicketTypeNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.repositories.TicketRepository;
import com.sarjeev.booktheshow.repositories.TicketSaleRepository;
import com.sarjeev.booktheshow.requests.CreateEventRequest;
import com.sarjeev.booktheshow.requests.CreateTicketTypeRequest;
import com.sarjeev.booktheshow.requests.UpdateEventRequest;
import com.sarjeev.booktheshow.requests.UpdateTicketTypeRequest;
import com.sarjeev.booktheshow.services.EventService;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import com.sarjeev.booktheshow.validators.EventScheduleValidator;
import com.sarjeev.booktheshow.validators.OwnershipValidator;
import com.sarjeev.booktheshow.validators.TicketTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketSaleRepository ticketSaleRepository;
    private final EventScheduleValidator eventScheduleValidator;
    private final TicketTypeValidator ticketTypeValidator;
    private final OwnershipValidator ownershipValidator;

    @Override
    @Transactional
    public Event createEvent(CreateEventRequest request) {
        User organizer = SecurityUtils.currentUser();
        eventScheduleValidator.validate(request.startDateTime(), request.endDateTime(), request.salesEndDate());

        Event event = Event.builder()
                .name(request.name().trim())
                .description(request.description())
                .venue(request.venue().trim())
                .imageUrl(request.imageUrl())
                .startDateTime(request.startDateTime())
                .endDateTime(request.endDateTime())
                .salesEndDate(request.salesEndDate())
                .status(request.status())
                .organizer(organizer)
                .build();

        request.ticketTypes().stream()
                .map(ticketTypeRequest -> createTicketTypeEntity(event, ticketTypeRequest))
                .forEach(event.getTicketTypes()::add);

        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> listEvents(Pageable pageable) {
        User currentUser = SecurityUtils.currentUser();
        if (SecurityUtils.isAdmin()) {
            return eventRepository.findAll(pageable);
        }
        return eventRepository.findByOrganizerId(currentUser.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        ownershipValidator.validateEventOwnerOrAdmin(event, SecurityUtils.currentUser());
        return event;
    }

    @Override
    @Transactional
    public Event updateEvent(UUID eventId, UpdateEventRequest request) {
        if (!eventId.equals(request.id())) {
            throw new BookTheShowException("Path event ID must match request event ID");
        }

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        ownershipValidator.validateEventOwnerOrAdmin(existingEvent, SecurityUtils.currentUser());
        eventScheduleValidator.validate(request.startDateTime(), request.endDateTime(), request.salesEndDate());
        eventScheduleValidator.validateStateTransition(existingEvent.getStatus(), request.status());

        existingEvent.setName(request.name().trim());
        existingEvent.setDescription(request.description());
        existingEvent.setVenue(request.venue().trim());
        existingEvent.setImageUrl(request.imageUrl());
        existingEvent.setStartDateTime(request.startDateTime());
        existingEvent.setEndDateTime(request.endDateTime());
        existingEvent.setSalesEndDate(request.salesEndDate());
        existingEvent.setStatus(request.status());

        Set<UUID> requestedExistingIds = request.ticketTypes().stream()
                .map(UpdateTicketTypeRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(ticketType -> {
            boolean removedFromRequest = ticketType.getId() != null && !requestedExistingIds.contains(ticketType.getId());
            if (removedFromRequest && ticketRepository.countByTicketTypeId(ticketType.getId()) > 0) {
                throw new BookTheShowException("Ticket types with sold tickets cannot be removed");
            }
            return removedFromRequest;
        });

        Map<UUID, TicketType> existingTicketTypes = existingEvent.getTicketTypes().stream()
                .filter(ticketType -> ticketType.getId() != null)
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketTypeRequest : request.ticketTypes()) {
            if (ticketTypeRequest.id() == null) {
                existingEvent.getTicketTypes().add(createTicketTypeEntity(existingEvent, ticketTypeRequest));
            } else {
                TicketType ticketType = existingTicketTypes.get(ticketTypeRequest.id());
                if (ticketType == null) {
                    throw new TicketTypeNotFoundException("Ticket type not found for this event");
                }
                updateTicketTypeEntity(ticketType, ticketTypeRequest);
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        ownershipValidator.validateEventOwnerOrAdmin(event, SecurityUtils.currentUser());
        if (ticketSaleRepository.countByEventId(eventId) > 0) {
            event.setStatus(EventStatusEnum.CANCELLED);
            eventRepository.save(event);
            return;
        }
        eventRepository.delete(event);
    }

    private TicketType createTicketTypeEntity(Event event, CreateTicketTypeRequest request) {
        ticketTypeValidator.validate(request.name(), request.price(), request.totalAvailable());
        return TicketType.builder()
                .name(request.name().trim().toUpperCase())
                .description(request.description())
                .price(request.price())
                .totalAvailable(request.totalAvailable())
                .remainingTickets(request.totalAvailable())
                .event(event)
                .build();
    }

    private TicketType createTicketTypeEntity(Event event, UpdateTicketTypeRequest request) {
        ticketTypeValidator.validate(request.name(), request.price(), request.totalAvailable());
        return TicketType.builder()
                .name(request.name().trim().toUpperCase())
                .description(request.description())
                .price(request.price())
                .totalAvailable(request.totalAvailable())
                .remainingTickets(request.totalAvailable())
                .event(event)
                .build();
    }

    private void updateTicketTypeEntity(TicketType ticketType, UpdateTicketTypeRequest request) {
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
    }
}
