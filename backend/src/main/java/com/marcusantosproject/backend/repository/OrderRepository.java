package com.marcusantosproject.backend.repository;

import com.marcusantosproject.backend.entities.Order;
import com.marcusantosproject.backend.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    boolean existsByAddress_AddressIdAndStatusIn(
            Long addressId,
            Collection<OrderStatus> statuses
    );
}
