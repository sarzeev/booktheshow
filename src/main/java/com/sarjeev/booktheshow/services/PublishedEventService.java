package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PublishedEventService {

    Page<Event> listPublishedEvents(String query, Pageable pageable);

    Event getPublishedEvent(UUID eventId);
}
