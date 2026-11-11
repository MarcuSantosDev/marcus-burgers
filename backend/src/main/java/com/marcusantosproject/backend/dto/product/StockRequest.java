package com.marcusantosproject.backend.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockRequest(
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}
