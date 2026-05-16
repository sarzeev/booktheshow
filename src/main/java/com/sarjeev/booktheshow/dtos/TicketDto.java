package com.sarjeev.booktheshow.dtos;

import com.sarjeev.booktheshow.enums.TicketStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketDto(
        UUID id,
        TicketStatusEnum status,
        LocalDateTime createdDateTime,
        UUID eventId,
        UUID ticketTypeId
) {
}
