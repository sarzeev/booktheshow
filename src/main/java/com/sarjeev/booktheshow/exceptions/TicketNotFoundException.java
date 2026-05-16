package com.sarjeev.booktheshow.exceptions;

public class TicketNotFoundException extends ResourceNotFoundException {

    public TicketNotFoundException(String message) {
        super(message);
    }
}
