package com.sarjeev.booktheshow.validators;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.User;
import com.sarjeev.booktheshow.exceptions.AccessDeniedException;
import com.sarjeev.booktheshow.utils.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class OwnershipValidator {

    public void validateEventOwnerOrAdmin(Event event, User user) {
        if (SecurityUtils.isAdmin()) {
            return;
        }
        if (event.getOrganizer() == null || !event.getOrganizer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only access events that you organize");
        }
    }
}
