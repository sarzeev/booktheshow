package com.sarjeev.booktheshow.exceptions;

public class BookTheShowException extends RuntimeException {

    public BookTheShowException(String message) {
        super(message);
    }

    public BookTheShowException(String message, Throwable cause) {
        super(message, cause);
    }
}
