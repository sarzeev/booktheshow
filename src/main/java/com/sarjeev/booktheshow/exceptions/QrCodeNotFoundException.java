package com.sarjeev.booktheshow.exceptions;

public class QrCodeNotFoundException extends ResourceNotFoundException {

    public QrCodeNotFoundException(String message) {
        super(message);
    }
}
