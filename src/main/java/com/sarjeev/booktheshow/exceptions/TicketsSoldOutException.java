package com.sarjeev.booktheshow.exceptions;

public class TicketsSoldOutException extends TicketUnavailableException {

    public TicketsSoldOutException(String message) {
        super(message);
    }
}
