package com.marcusantosproject.backend.services.exceptions;

public class CategoryHasProducts extends RuntimeException {
    public CategoryHasProducts(Long id) {
        super("Category with ID " + id + " cannot be deleted because it has products.");
    }
}