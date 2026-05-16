package com.sarjeev.booktheshow.responses;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        boolean enabled,
        LocalDateTime createdAt,
        Set<String> roles
) {
}
