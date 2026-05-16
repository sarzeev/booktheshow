package com.sarjeev.booktheshow.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DashboardSummaryResponse(
        UUID eventId,
        String eventName,
        BigDecimal revenue,
        long completedSales,
        long ticketsSold,
        long activeTickets,
        long usedTickets,
        long validationAttempts,
        long successfulValidations,
        long duplicateValidations,
        List<TicketInventoryResponse> inventory
) {
}
