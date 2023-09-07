package com.murat.invoice.generation.exception;

public class ResourceAlreadyExists extends RuntimeException{
    public ResourceAlreadyExists(String message, Exception e) {
        super(message, e);
    }

    public ResourceAlreadyExists(String message) {
        super(message);
    }
}