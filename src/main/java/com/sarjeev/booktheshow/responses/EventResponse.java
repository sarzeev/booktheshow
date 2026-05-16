package com.sarjeev.booktheshow.responses;

import com.sarjeev.booktheshow.enums.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String name,
        String description,
        String venue,
        String imageUrl,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime salesEndDate,
        EventStatusEnum status,
        UUID organizerId,
        List<TicketTypeResponse> ticketTypes
) {
}
