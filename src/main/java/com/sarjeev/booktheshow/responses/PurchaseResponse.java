package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.TicketSaleStatusEnum;
import com.sarjeev.booktheshow.enums.TicketStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PurchaseResponse(
        UUID saleId,
        UUID ticketId,
        UUID qrCodeId,
        UUID eventId,
        UUID ticketTypeId,
        BigDecimal amount,
        TicketSaleStatusEnum saleStatus,
        TicketStatusEnum ticketStatus,
        LocalDateTime purchaseDateTime
) {
}
