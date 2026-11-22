package com.marcusantosproject.backend.dto.order;

import com.marcusantosproject.backend.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderUpdate(
        @NotNull(message = "Order status is required")
        OrderStatus orderStatus
) {}