package com.sarjeev.booktheshow.controllers;

import com.sarjeev.booktheshow.entities.Ticket;
import com.sarjeev.booktheshow.mappers.TicketMapper;
import com.sarjeev.booktheshow.responses.ApiResponse;
import com.sarjeev.booktheshow.responses.QrCodeImageResponse;
import com.sarjeev.booktheshow.responses.TicketResponse;
import com.sarjeev.booktheshow.services.QrCodeService;
import com.sarjeev.booktheshow.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
@PreAuthorize("hasAnyRole('ADMIN','ATTENDEE')")
public class TicketController {

    private final TicketService ticketService;
    private final QrCodeService qrCodeService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TicketResponse>>> listTickets(Pageable pageable) {
        Page<TicketResponse> response = ticketService.listTicketsForCurrentAttendee(pageable)
                .map(ticketMapper::toTicketResponse);
        return ResponseEntity.ok(ApiResponse.success("Tickets retrieved successfully", response));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicket(@PathVariable UUID ticketId) {
        Ticket ticket = ticketService.getTicketForCurrentAttendee(ticketId);
        return ResponseEntity.ok(ApiResponse.success("Ticket retrieved successfully", ticketMapper.toTicketResponse(ticket)));
    }

    @GetMapping("/{ticketId}/qr-code")
    public ResponseEntity<byte[]> getTicketQrCodeImage(@PathVariable UUID ticketId) {
        Ticket ticket = ticketService.getTicketForCurrentAttendee(ticketId);
        byte[] image = qrCodeService.getQrCodeImageForTicket(ticket.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"ticket-%s-qr.png\"".formatted(ticketId))
                .contentType(MediaType.IMAGE_PNG)
                .contentLength(image.length)
                .body(image);
    }

    @GetMapping("/{ticketId}/qr-code/data")
    public ResponseEntity<ApiResponse<QrCodeImageResponse>> getTicketQrCodeData(@PathVariable UUID ticketId) {
        Ticket ticket = ticketService.getTicketForCurrentAttendee(ticketId);
        var qrCode = qrCodeService.getQrCodeForTicket(ticket.getId());
        QrCodeImageResponse response = new QrCodeImageResponse(qrCode.getId(), ticket.getId(), qrCode.getQrCodeData());
        return ResponseEntity.ok(ApiResponse.success("Ticket QR code retrieved successfully", response));
    }
}
