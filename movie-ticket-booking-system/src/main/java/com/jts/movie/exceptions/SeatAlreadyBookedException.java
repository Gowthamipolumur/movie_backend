package com.jts.movie.exceptions;

public class SeatAlreadyBookedException extends RuntimeException {

    // Constructor that accepts a message
    public SeatAlreadyBookedException(String message) {
        super(message);
    }

    // Constructor that accepts both message and cause
    public SeatAlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }
}
