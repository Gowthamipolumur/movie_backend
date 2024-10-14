package com.jts.movie.exceptions;

public class UserExist extends RuntimeException {

    // Default constructor (no arguments)
    public UserExist() {
        super("User already exists.");
    }

    // Constructor with a custom message
    public UserExist(String message) {
        super(message);
    }
}
