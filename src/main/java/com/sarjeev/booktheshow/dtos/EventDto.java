package com.sarjeev.booktheshow.dtos;

import com.sarjeev.booktheshow.enums.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventDto(
        UUID id,
        String name,
        String venue,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        EventStatusEnum status
) {
}
