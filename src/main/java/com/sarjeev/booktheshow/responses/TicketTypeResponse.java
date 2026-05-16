package com.sarjeev.booktheshow.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer totalAvailable,
        Integer remainingTickets
) {
}
