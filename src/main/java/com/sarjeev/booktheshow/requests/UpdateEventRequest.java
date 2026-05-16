package com.sarjeev.booktheshow.requests;

import com.sarjeev.booktheshow.enums.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateEventRequest(
        @NotNull(message = "Event ID is required")
        UUID id,

        @NotBlank(message = "Event name is required")
        @Size(max = 200, message = "Event name must not exceed 200 characters")
        String name,

        String description,

        @NotBlank(message = "Venue is required")
        @Size(max = 300, message = "Venue must not exceed 300 characters")
        String venue,

        @Size(max = 1000, message = "Image URL must not exceed 1000 characters")
        String imageUrl,

        @NotNull(message = "Start date and time is required")
        LocalDateTime startDateTime,

        @NotNull(message = "End date and time is required")
        LocalDateTime endDateTime,

        @NotNull(message = "Sales end date is required")
        LocalDateTime salesEndDate,

        @NotNull(message = "Event status is required")
        EventStatusEnum status,

        @NotEmpty(message = "At least one ticket type is required")
        @Valid
        List<UpdateTicketTypeRequest> ticketTypes
) {
}
