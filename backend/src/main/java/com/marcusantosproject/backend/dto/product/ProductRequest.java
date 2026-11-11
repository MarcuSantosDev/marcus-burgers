package com.marcusantosproject.backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100)
        String name,

        String description,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must not be negative")
        BigDecimal price,

        @PositiveOrZero(message = "Stock must not be negative")
        int stock
) {}