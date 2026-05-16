package com.sarjeev.booktheshow.utils;

import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.exceptions.AuthenticationFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new AuthenticationFailedException("Authenticated user is unavailable");
        }
        return user;
    }

    public static UUID currentUserId() {
        return currentUser().getId();
    }

    public static boolean hasRole(String roleName) {
        String normalizedRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        return currentUser().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(normalizedRoleName));
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
}
