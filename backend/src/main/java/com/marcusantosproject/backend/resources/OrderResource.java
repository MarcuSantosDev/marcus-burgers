package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.order.OrderRequest;
import com.marcusantosproject.backend.dto.order.OrderResponse;
import com.marcusantosproject.backend.dto.order.OrderUpdate;
import com.marcusantosproject.backend.security.AuthUtils;
import com.marcusantosproject.backend.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderResource {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok().body(orderService.findAll(AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id,
                                                  @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok().body(orderService.findById(id, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> insert(@RequestBody @Valid OrderRequest request,
                                                @AuthenticationPrincipal Jwt jwt) {
        OrderResponse response = orderService.insert(AuthUtils.userId(jwt), request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.orderId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    // Sem @PreAuthorize: o CLIENT pode cancelar o próprio pedido PENDING.
    // As regras por perfil ficam no OrderService.update.
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid OrderUpdate request,
                                                @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.update(id, request, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}