package com.marcusantosproject.backend.services.exceptions;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super("message");
    }
}
