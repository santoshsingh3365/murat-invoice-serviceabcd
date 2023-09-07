package com.murat.invoice.generation.exception;

public class InvalidRequest extends RuntimeException{
    public InvalidRequest(String message, Exception e) {
        super(message, e);
    }

    public InvalidRequest(String message) {
        super(message);
    }
}
