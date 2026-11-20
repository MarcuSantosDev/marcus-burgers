package com.marcusantosproject.backend.dto.OrderItem;

import com.marcusantosproject.backend.entities.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long orderItemId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    public OrderItemResponse(OrderItem item) {
        this(
                item.getOrderItemId(),
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}