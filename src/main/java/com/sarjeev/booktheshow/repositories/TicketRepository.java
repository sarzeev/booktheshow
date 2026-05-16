package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.enums.TicketStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event", "ticketSale", "qrCode"})
    Page<Ticket> findByAttendeeId(UUID attendeeId, Pageable pageable);

    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event", "ticketSale", "qrCode"})
    Optional<Ticket> findByIdAndAttendeeId(UUID id, UUID attendeeId);

    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event", "ticketSale", "qrCode"})
    Page<Ticket> findByTicketTypeEventId(UUID eventId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event", "ticketSale", "qrCode"})
    Optional<Ticket> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"attendee", "ticketType", "ticketType.event", "ticketSale", "qrCode"})
    Page<Ticket> findAll(Pageable pageable);

    long countByTicketTypeId(UUID ticketTypeId);

    long countByTicketTypeIdAndStatus(UUID ticketTypeId, TicketStatusEnum status);

    long countByTicketTypeEventId(UUID eventId);

    long countByTicketTypeEventIdAndStatus(UUID eventId, TicketStatusEnum status);
}
