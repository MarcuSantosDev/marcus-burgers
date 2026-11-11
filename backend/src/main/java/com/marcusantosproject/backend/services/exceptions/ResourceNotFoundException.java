package com.marcusantosproject.backend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    static final long serialVersionUID = 1L;
    public ResourceNotFoundException(Object id){
        super("Resource not found. ID: " + id);
    }
}
