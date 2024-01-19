package com.example.hotelservice.Exceptions;

public class HotelsNotFoundException extends RuntimeException {
    public HotelsNotFoundException() {
    }

    public HotelsNotFoundException(String message) {
        super(message);
    }

    public HotelsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HotelsNotFoundException(Throwable cause) {
        super(cause);
    }

    public HotelsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
