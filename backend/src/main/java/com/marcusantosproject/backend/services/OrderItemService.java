package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.entities.Order;
import com.marcusantosproject.backend.repository.OrderRepository;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<OrderItemResponse> findAll(Long orderId, Long callerId, boolean isAdmin) {
        Order order = findOrderById(orderId);

        // Se o pedido é de outro usuário, responde 404 para não revelar que ele existe
        if (!isAdmin && !order.getUser().getUserId().equals(callerId)) {
            throw new ResourceNotFound("Order not found. Id: " + orderId);
        }

        return order.getItems().stream().map(OrderItemResponse::new).toList();
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound("Order not found. Id: " + orderId));
    }
}