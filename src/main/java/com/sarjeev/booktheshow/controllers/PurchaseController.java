package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.PurchaseResponse;
import com.sarjeev.booktheshow.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/published-events/{eventId}/ticket-types/{ticketTypeId}")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/purchase")
    @PreAuthorize("hasAnyRole('ADMIN','ATTENDEE')")
    public ResponseEntity<ApiResponse<PurchaseResponse>> purchaseTicket(
            @PathVariable UUID eventId,
            @PathVariable UUID ticketTypeId
    ) {
        PurchaseResponse response = purchaseService.purchaseTicket(eventId, ticketTypeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Ticket purchased successfully", response));
    }
}
