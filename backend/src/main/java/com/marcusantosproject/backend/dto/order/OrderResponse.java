package com.marcusantosproject.backend.dto.order;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        LocalDateTime orderDate,
        OrderStatus status,
        BigDecimal totalAmount,
        Long addressId,
        List<OrderItemResponse> items
) {}
