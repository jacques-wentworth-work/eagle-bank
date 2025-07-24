package com.eaglebank.exception;

public class AccountNotFoundException extends RuntimeException {
    public static final String STANDARD_EXCEPTION_MESSAGE = "Account (%s) not found";

    public AccountNotFoundException(String message) {
        super(message);
    }
}
