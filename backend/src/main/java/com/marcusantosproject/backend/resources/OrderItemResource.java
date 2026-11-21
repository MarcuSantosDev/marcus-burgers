package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemRequest;
import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.entities.OrderItem;
import com.marcusantosproject.backend.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/orders-item")
@RequiredArgsConstructor
public class OrderItemResource {
    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> findAll(){
        return ResponseEntity.ok().body(orderItemService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderItemResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(orderItemService.findById(id));
    }

}
