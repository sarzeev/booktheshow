package com.sarjeev.booktheshow.exceptions;

public class SalesClosedException extends TicketUnavailableException {

    public SalesClosedException(String message) {
        super(message);
    }
}
