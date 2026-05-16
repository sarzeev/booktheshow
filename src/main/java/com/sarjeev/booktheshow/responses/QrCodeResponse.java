package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.QrCodeStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record QrCodeResponse(
        UUID id,
        String qrCodeData,
        LocalDateTime generatedDateTime,
        QrCodeStatusEnum status,
        UUID ticketId
) {
}
