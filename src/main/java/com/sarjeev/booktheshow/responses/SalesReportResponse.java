package com.sarjeev.booktheshow.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesReportResponse(
        UUID eventId,
        String eventName,
        BigDecimal revenue,
        long pendingSales,
        long completedSales,
        long failedSales,
        long refundedSales,
        long attendeeCount
) {
}
