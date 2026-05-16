package com.sarjeev.booktheshow.responses;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors,
        Instant timestamp
) {
    public static ErrorResponse of(String error, String message, String path) {
        return new ErrorResponse(false, error, message, path, Map.of(), Instant.now());
    }

    public static ErrorResponse validation(String message, String path, Map<String, String> validationErrors) {
        return new ErrorResponse(false, "VALIDATION_ERROR", message, path, validationErrors, Instant.now());
    }
}
