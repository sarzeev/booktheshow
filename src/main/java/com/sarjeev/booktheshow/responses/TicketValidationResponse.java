package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.TicketValidationStatusEnum;
import com.sarjeev.booktheshow.enums.ValidationMethodEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketValidationResponse(
        UUID id,
        TicketValidationStatusEnum status,
        LocalDateTime validationTime,
        ValidationMethodEnum validationMethod,
        UUID ticketId,
        UUID validatedById
) {
}
