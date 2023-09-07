package com.murat.invoice.generation.exception;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String message, Exception e) {
        super(message, e);
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
