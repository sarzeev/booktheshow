package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.mappers.TicketMapper;
import com.sarjeev.booktheshow.requests.TicketValidationRequest;
import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.TicketValidationResponse;
import com.sarjeev.booktheshow.services.TicketValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/ticket-validations")
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketMapper ticketMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiResponse<TicketValidationResponse>> validateTicket(
            @PathVariable UUID eventId,
            @Valid @RequestBody TicketValidationRequest request
    ) {
        TicketValidationResponse response = ticketMapper.toTicketValidationResponse(
                ticketValidationService.validateTicket(eventId, request)
        );
        return ResponseEntity.ok(ApiResponse.success("Ticket validation completed", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER','STAFF')")
    public ResponseEntity<ApiResponse<Page<TicketValidationResponse>>> listValidations(
            @PathVariable UUID eventId,
            Pageable pageable
    ) {
        Page<TicketValidationResponse> response = ticketValidationService.listValidations(eventId, pageable)
                .map(ticketMapper::toTicketValidationResponse);
        return ResponseEntity.ok(ApiResponse.success("Ticket validations retrieved successfully", response));
    }
}
