package com.sarjeev.booktheshow.responses;

import java.util.UUID;

public record ValidationReportResponse(
        UUID eventId,
        String eventName,
        long totalAttempts,
        long successfulValidations,
        long failedValidations,
        long duplicateValidations,
        long invalidValidations
) {
}
