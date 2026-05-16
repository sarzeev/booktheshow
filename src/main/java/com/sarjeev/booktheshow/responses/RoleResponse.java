package com.sarjeev.booktheshow.responses;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name
) {
}
