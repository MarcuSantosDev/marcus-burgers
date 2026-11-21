package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemRequest;
import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.dto.order.OrderResponse;
import com.marcusantosproject.backend.entities.OrderItem;
import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.repository.OrderItemRepository;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
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

    public OrderItemResponse findById(Long id){
        return new OrderItemResponse(findOrderItemById(id));
    }


    private OrderItem findOrderItemById(Long id){
        return orderItemRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }
}
