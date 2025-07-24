package com.eaglebank.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String STANDARD_EXCEPTION_MESSAGE = "User (%s) not found";

    public UserNotFoundException(String message) {
        super(message);
    }
}
