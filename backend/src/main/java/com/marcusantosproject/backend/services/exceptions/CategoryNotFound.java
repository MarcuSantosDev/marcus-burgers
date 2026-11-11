package com.marcusantosproject.backend.services.exceptions;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(Long id) {
        super("Category not found. ID: " + id);
    }
}