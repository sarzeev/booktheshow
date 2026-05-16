package com.sarjeev.booktheshow.validators;

import com.sarjeev.booktheshow.entities.Event;
import com.sarjeev.booktheshow.entities.TicketType;
import com.sarjeev.booktheshow.enums.EventStatusEnum;
import com.sarjeev.booktheshow.exceptions.SalesClosedException;
import com.sarjeev.booktheshow.exceptions.TicketUnavailableException;
import com.sarjeev.booktheshow.exceptions.TicketsSoldOutException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PurchaseValidator {

    public void validateTicketCanBePurchased(Event event, TicketType ticketType) {
        if (event.getStatus() != EventStatusEnum.PUBLISHED) {
            throw new TicketUnavailableException("Tickets can only be purchased for published events");
        }
        if (!ticketType.getEvent().getId().equals(event.getId())) {
            throw new TicketUnavailableException("Ticket type does not belong to the selected event");
        }
        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new TicketUnavailableException("Tickets cannot be purchased for events that have already started");
        }
        if (event.getSalesEndDate().isBefore(LocalDateTime.now())) {
            throw new SalesClosedException("Ticket sales have ended for this event");
        }
        if (ticketType.getRemainingTickets() == null || ticketType.getRemainingTickets() <= 0) {
            throw new TicketsSoldOutException("This ticket type is sold out");
        }
    }
}
