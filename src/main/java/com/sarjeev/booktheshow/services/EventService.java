package com.sarjeev.booktheshow.services;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.requests.CreateEventRequest;
import com.sarjeev.booktheshow.requests.UpdateEventRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {

    Event createEvent(CreateEventRequest request);

    Page<Event> listEvents(Pageable pageable);

    Event getEvent(UUID eventId);

    Event updateEvent(UUID eventId, UpdateEventRequest request);

    void deleteEvent(UUID eventId);
}
