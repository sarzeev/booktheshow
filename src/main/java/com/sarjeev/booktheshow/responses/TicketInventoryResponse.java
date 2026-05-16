package com.sarjeev.booktheshow.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketInventoryResponse(
        UUID ticketTypeId,
        String name,
        BigDecimal price,
        Integer totalAvailable,
        Integer remainingTickets,
        Integer soldTickets
) {
}
