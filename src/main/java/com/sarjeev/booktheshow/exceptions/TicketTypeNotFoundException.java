package com.sarjeev.booktheshow.exceptions;

public class TicketTypeNotFoundException extends ResourceNotFoundException {

    public TicketTypeNotFoundException(String message) {
        super(message);
    }
}
