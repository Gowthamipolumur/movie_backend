package com.jts.movie.exceptions;

public class InvalidPromoException extends RuntimeException {

    // Constructor that accepts a message
    public InvalidPromoException(String message) {
        super(message);
    }

    // Constructor that accepts both message and cause
    public InvalidPromoException(String message, Throwable cause) {
        super(message, cause);
    }
}
