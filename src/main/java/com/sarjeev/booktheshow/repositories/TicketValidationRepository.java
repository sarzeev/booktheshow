package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.TicketValidation;
import com.sarjeev.booktheshow.enums.TicketValidationStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {

    @EntityGraph(attributePaths = {"ticket", "ticket.ticketType", "ticket.ticketType.event", "validatedBy"})
    Page<TicketValidation> findByTicketTicketTypeEventId(UUID eventId, Pageable pageable);

    @EntityGraph(attributePaths = {"ticket", "ticket.ticketType", "ticket.ticketType.event", "validatedBy"})
    Page<TicketValidation> findByValidatedById(UUID validatedById, Pageable pageable);

    boolean existsByTicketIdAndStatus(UUID ticketId, TicketValidationStatusEnum status);

    long countByTicketTicketTypeEventId(UUID eventId);

    long countByTicketTicketTypeEventIdAndStatus(UUID eventId, TicketValidationStatusEnum status);
}
