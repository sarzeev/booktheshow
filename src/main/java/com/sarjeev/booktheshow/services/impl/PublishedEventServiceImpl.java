package com.sarjeev.booktheshow.services.impl;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.enums.EventStatusEnum;
import com.sarjeev.booktheshow.exceptions.EventNotFoundException;
import com.sarjeev.booktheshow.repositories.EventRepository;
import com.sarjeev.booktheshow.services.PublishedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublishedEventServiceImpl implements PublishedEventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Event> listPublishedEvents(String query, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            return eventRepository.searchPublishedEvents(query.trim(), pageable);
        }
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getPublishedEvent(UUID eventId) {
        return eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException("Published event not found"));
    }
}
