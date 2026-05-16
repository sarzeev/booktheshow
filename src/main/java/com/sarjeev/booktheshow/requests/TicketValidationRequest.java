package com.sarjeev.booktheshow.requests;

import com.sarjeev.booktheshow.enums.ValidationMethodEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketValidationRequest(
        @NotBlank(message = "Ticket ID, QR code ID, or scan payload is required")
        String id,

        @NotNull(message = "Validation method is required")
        ValidationMethodEnum validationMethod
) {
}
