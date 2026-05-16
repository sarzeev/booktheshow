package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.TicketSaleStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketSaleResponse(
        UUID id,
        BigDecimal amount,
        TicketSaleStatusEnum status,
        LocalDateTime purchaseDateTime,
        UUID purchaserId,
        UUID eventId
) {
}
