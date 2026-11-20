package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders-item")
@RequiredArgsConstructor
public class OrderItemResource {
    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> findAll(){
        return ResponseEntity.ok().body(orderItemService.findAll());
    }

}
