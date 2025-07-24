package com.eaglebank.exception;

public class TransactionNotFoundException extends RuntimeException {
    public static final String STANDARD_EXCEPTION_MESSAGE = "Transaction (%s) not found";

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
