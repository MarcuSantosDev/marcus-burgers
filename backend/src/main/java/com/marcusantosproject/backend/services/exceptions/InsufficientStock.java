package com.marcusantosproject.backend.services.exceptions;

public class InsufficientStock extends RuntimeException {
    public InsufficientStock(String productName, int requested, int available) {
        super("Insufficient stock for " + productName
                + ". Requested: " + requested
                + ", Available: " + available);
    }
}
