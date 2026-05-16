package com.sarjeev.booktheshow.dtos;

import java.util.Set;
import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Set<String> roles
) {
}
