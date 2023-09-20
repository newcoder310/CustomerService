package com.api.customers.exception;

public class CustomerNotPresentException extends RuntimeException {

    public CustomerNotPresentException(String message) {
        super(message);
    }
}
