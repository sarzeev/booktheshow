package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.mappers.EventMapper;
import com.sarjeev.booktheshow.requests.CreateTicketTypeRequest;
import com.sarjeev.booktheshow.requests.UpdateTicketTypeRequest;
import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.TicketTypeResponse;
import com.sarjeev.booktheshow.services.TicketTypeService;
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
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TicketTypeResponse>> createTicketType(
            @PathVariable UUID eventId,
            @Valid @RequestBody CreateTicketTypeRequest request
    ) {
        TicketTypeResponse response = eventMapper.toTicketTypeResponse(ticketTypeService.createTicketType(eventId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Ticket type created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TicketTypeResponse>>> listTicketTypes(@PathVariable UUID eventId, Pageable pageable) {
        Page<TicketTypeResponse> response = ticketTypeService.listTicketTypes(eventId, pageable)
                .map(eventMapper::toTicketTypeResponse);
        return ResponseEntity.ok(ApiResponse.success("Ticket types retrieved successfully", response));
    }

    @PutMapping("/{ticketTypeId}")
    public ResponseEntity<ApiResponse<TicketTypeResponse>> updateTicketType(
            @PathVariable UUID eventId,
            @PathVariable UUID ticketTypeId,
            @Valid @RequestBody UpdateTicketTypeRequest request
    ) {
        TicketTypeResponse response = eventMapper.toTicketTypeResponse(ticketTypeService.updateTicketType(eventId, ticketTypeId, request));
        return ResponseEntity.ok(ApiResponse.success("Ticket type updated successfully", response));
    }

    @DeleteMapping("/{ticketTypeId}")
    public ResponseEntity<ApiResponse<Void>> deleteTicketType(@PathVariable UUID eventId, @PathVariable UUID ticketTypeId) {
        ticketTypeService.deleteTicketType(eventId, ticketTypeId);
        return ResponseEntity.ok(ApiResponse.success("Ticket type deleted successfully", null));
    }
}
