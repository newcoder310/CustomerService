package com.api.customers.exception;

public class ExternalCallFailedException extends RuntimeException {
    public ExternalCallFailedException(String message) {
        super(message);
    }
}
