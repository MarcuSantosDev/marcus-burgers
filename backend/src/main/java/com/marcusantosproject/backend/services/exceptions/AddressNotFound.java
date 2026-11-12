package com.marcusantosproject.backend.services.exceptions;

public class AddressNotFound extends RuntimeException {
    public AddressNotFound(Long id) {
        super("Category not found. ID: " + id);
    }
}
