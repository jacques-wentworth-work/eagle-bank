package com.eaglebank.exception;

public class UnprocessedTransactionException extends RuntimeException {
    public UnprocessedTransactionException(String message) {
        super(message);
    }
}
