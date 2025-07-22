package com.eaglebank.exception;

public class UserAuthNotFoundException extends RuntimeException {
    public UserAuthNotFoundException(String message) {
        super(message);
    }
}
