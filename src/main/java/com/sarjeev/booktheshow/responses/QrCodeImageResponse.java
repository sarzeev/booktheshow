package com.sarjeev.booktheshow.responses;

import java.util.UUID;

public record QrCodeImageResponse(
        UUID qrCodeId,
        UUID ticketId,
        String qrCodeData
) {
}
