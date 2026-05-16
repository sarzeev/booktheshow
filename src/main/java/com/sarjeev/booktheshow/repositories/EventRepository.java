package com.sarjeev.booktheshow.repositories;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.enums.EventStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Page<Event> findByStatus(EventStatusEnum status, Pageable pageable);

    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Optional<Event> findByIdAndStatus(UUID id, EventStatusEnum status);

    Page<Event> findByStartDateTimeAfter(LocalDateTime startDateTime, Pageable pageable);

    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    @Query(value = """
            SELECT * FROM events
            WHERE status = 'PUBLISHED'
              AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '') || ' ' || COALESCE(description, ''))
                  @@ plainto_tsquery('english', :searchTerm)
            """,
            countQuery = """
            SELECT count(*) FROM events
            WHERE status = 'PUBLISHED'
              AND to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(venue, '') || ' ' || COALESCE(description, ''))
                  @@ plainto_tsquery('english', :searchTerm)
            """,
            nativeQuery = true)
    Page<Event> searchPublishedEvents(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Optional<Event> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"ticketTypes", "organizer"})
    Page<Event> findAll(Pageable pageable);
}
