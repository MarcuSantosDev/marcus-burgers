package com.marcusantosproject.backend.repository;

import com.marcusantosproject.backend.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    boolean existsByProductProductId(Long productId);
}
