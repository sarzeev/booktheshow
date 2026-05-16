package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.TicketType;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    List<TicketType> findByEventId(UUID eventId);

    Page<TicketType> findByEventId(UUID eventId, Pageable pageable);

    Optional<TicketType> findByIdAndEventId(UUID id, UUID eventId);

    boolean existsByIdAndEventId(UUID id, UUID eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ticketType from TicketType ticketType where ticketType.id = :id")
    Optional<TicketType> findByIdWithLock(@Param("id") UUID id);
}
