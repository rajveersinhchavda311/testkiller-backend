package com.testkiller.backend.exception;

public class InactiveUserException extends RuntimeException {

    public InactiveUserException(String message) {
        super(message);
    }
}
