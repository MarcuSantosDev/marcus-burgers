package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemResponse;
import com.marcusantosproject.backend.security.AuthUtils;
import com.marcusantosproject.backend.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemResource {
    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> findAll(@PathVariable Long orderId,
                                                           @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok().body(
                orderItemService.findAll(orderId, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt)));
    }
}