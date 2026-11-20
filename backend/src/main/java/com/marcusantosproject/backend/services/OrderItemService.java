package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public List<OrderItemResponse> findAll(){
        return orderItemRepository.findAll().stream().map(OrderItemResponse::new).toList();
    }
}
