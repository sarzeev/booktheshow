package com.sarjeev.booktheshow.exceptions;

public class EventNotFoundException extends ResourceNotFoundException {

    public EventNotFoundException(String message) {
        super(message);
    }
}
