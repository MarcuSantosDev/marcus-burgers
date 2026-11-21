package com.marcusantosproject.backend.dto.order;

import com.marcusantosproject.backend.entities.Order;
import com.marcusantosproject.backend.enums.OrderStatus;

public record OrderUpdate(OrderStatus orderStatus) {

    OrderUpdate(Order order){
        this(order.getStatus());
    }
}
