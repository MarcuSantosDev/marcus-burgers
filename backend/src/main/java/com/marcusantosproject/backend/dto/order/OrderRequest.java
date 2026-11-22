package com.marcusantosproject.backend.dto.order;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull
        Long addressId,

        @NotEmpty @Valid
        List<OrderItemRequest> items
) {}