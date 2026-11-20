package com.marcusantosproject.backend.dto.OrderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(

        @NotNull
        Long productId,

        @Positive
        int quantity

) {}
