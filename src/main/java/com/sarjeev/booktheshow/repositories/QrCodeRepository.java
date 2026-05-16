package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.QrCode;
import com.sarjeev.booktheshow.enums.QrCodeStatusEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    @EntityGraph(attributePaths = {"ticket", "ticket.attendee", "ticket.ticketType", "ticket.ticketType.event"})
    Optional<QrCode> findByTicketId(UUID ticketId);

    @EntityGraph(attributePaths = {"ticket", "ticket.attendee", "ticket.ticketType", "ticket.ticketType.event"})
    Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);

    @EntityGraph(attributePaths = {"ticket", "ticket.attendee", "ticket.ticketType", "ticket.ticketType.event"})
    Optional<QrCode> findByTicketIdAndTicketAttendeeId(UUID ticketId, UUID attendeeId);

    @Override
    @EntityGraph(attributePaths = {"ticket", "ticket.attendee", "ticket.ticketType", "ticket.ticketType.event"})
    Optional<QrCode> findById(UUID id);
}
