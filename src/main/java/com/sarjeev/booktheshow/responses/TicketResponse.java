package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.TicketStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        TicketStatusEnum status,
        LocalDateTime createdDateTime,
        UUID attendeeId,
        UUID ticketTypeId,
        UUID ticketSaleId,
        UUID qrCodeId
) {
}
