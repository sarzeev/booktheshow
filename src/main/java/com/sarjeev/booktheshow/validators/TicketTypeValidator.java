package com.sarjeev.booktheshow.validators;

import com.sarjeev.booktheshow.exceptions.BookTheShowException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class TicketTypeValidator {

    private static final Set<String> STANDARD_TICKET_NAMES = Set.of("VIP", "STANDARD", "PREMIUM");

    public void validate(String name, BigDecimal price, Integer totalAvailable) {
        if (name == null || name.isBlank()) {
            throw new BookTheShowException("Ticket type name is required");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BookTheShowException("Ticket price cannot be negative");
        }
        if (totalAvailable == null || totalAvailable < 1) {
            throw new BookTheShowException("Ticket quantity must be at least 1");
        }
    }

    public boolean isStandardTicketTypeName(String name) {
        return name != null && STANDARD_TICKET_NAMES.contains(name.trim().toUpperCase());
    }
}
