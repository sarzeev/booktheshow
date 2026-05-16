package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.mappers.EventMapper;
import com.sarjeev.booktheshow.requests.CreateEventRequest;
import com.sarjeev.booktheshow.requests.UpdateEventRequest;
import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.EventResponse;
import com.sarjeev.booktheshow.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        EventResponse response = eventMapper.toResponse(eventService.createEvent(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Event created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventResponse>>> listEvents(Pageable pageable) {
        Page<EventResponse> response = eventService.listEvents(pageable).map(eventMapper::toResponse);
        return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", response));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> getEvent(@PathVariable UUID eventId) {
        EventResponse response = eventMapper.toResponse(eventService.getEvent(eventId));
        return ResponseEntity.ok(ApiResponse.success("Event retrieved successfully", response));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        EventResponse response = eventMapper.toResponse(eventService.updateEvent(eventId, request));
        return ResponseEntity.ok(ApiResponse.success("Event updated successfully", response));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(ApiResponse.success("Event deleted successfully", null));
    }
}
