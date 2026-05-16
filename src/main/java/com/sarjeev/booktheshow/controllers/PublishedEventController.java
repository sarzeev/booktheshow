package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.mappers.EventMapper;
import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.EventResponse;
import com.sarjeev.booktheshow.services.PublishedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/published-events")
public class PublishedEventController {

    private final PublishedEventService publishedEventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventResponse>>> listPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        Page<EventResponse> response = publishedEventService.listPublishedEvents(q, pageable).map(eventMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success("Published events retrieved successfully", response));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getPublishedEvent(@PathVariable UUID eventId) {
        EventResponse response = eventMapper.toResponse(publishedEventService.getPublishedEvent(eventId));
        return ResponseEntity.ok(ApiResponse.success("Published event retrieved successfully", response));
    }
}
