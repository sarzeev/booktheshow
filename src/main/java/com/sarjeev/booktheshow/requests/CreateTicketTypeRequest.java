package com.sarjeev.booktheshow.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateTicketTypeRequest(
        @NotBlank(message = "Ticket type name is required")
        @Size(max = 120, message = "Ticket type name must not exceed 120 characters")
        String name,

        String description,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be zero or greater")
        BigDecimal price,

        @NotNull(message = "Total availability is required")
        @Min(value = 1, message = "Total availability must be at least 1")
        Integer totalAvailable
) {
}
