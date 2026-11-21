package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.order.OrderRequest;
import com.marcusantosproject.backend.dto.order.OrderResponse;
import com.marcusantosproject.backend.entities.Order;
import com.marcusantosproject.backend.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderResource {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(){
        return ResponseEntity.ok().body(orderService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> insert(@RequestBody @Valid OrderRequest request, Principal principal){
        OrderResponse response = orderService.insert(request,principal.getName());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.orderId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }


}
