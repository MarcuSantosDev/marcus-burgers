package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemRequest;
import com.marcusantosproject.backend.dto.order.OrderRequest;
import com.marcusantosproject.backend.dto.order.OrderResponse;
import com.marcusantosproject.backend.dto.order.OrderUpdate;
import com.marcusantosproject.backend.entities.*;
import com.marcusantosproject.backend.enums.OrderStatus;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.repository.OrderRepository;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.exceptions.BadRequest;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // ADMIN vê todos os pedidos; CLIENT vê só os seus
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(Long callerId, boolean isAdmin) {
        List<Order> orders = isAdmin
                ? orderRepository.findAll()
                : orderRepository.findByUserUserId(callerId);
        return orders.stream().map(OrderResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id, Long callerId, boolean isAdmin) {
        return new OrderResponse(findOrderForCaller(id, callerId, isAdmin));
    }

    // O dono do pedido é sempre quem está logado (callerId vem do token, nunca do
    // body)
    @Transactional
    public OrderResponse insert(Long callerId, boolean isAdmin, OrderRequest request) {
        if (isAdmin) {
            throw new BadRequest("Administradores não podem fazer pedidos.");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new BadRequest("Order must have at least one item");
        }

        User user = userRepository.findById(callerId)
                .orElseThrow(() -> new ResourceNotFound(callerId));

        // Garante que o endereço pertence ao usuário e não foi removido (soft delete)
        Address address = addressRepository
                .findByAddressIdAndUserUserIdAndActiveTrue(request.addressId(), user.getUserId())
                .orElseThrow(() -> new ResourceNotFound(
                        "Address not found, removed, or does not belong to the user."));

        Order order = Order.builder().user(user).address(address).build();

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = findProductById(itemRequest.productId());
            product.removeStock(itemRequest.quantity());
            order.addItem(OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .unitPrice(product.getPrice())
                    .build());
        }

        return new OrderResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse update(Long id, OrderUpdate request, Long callerId, boolean isAdmin) {
        Order order = findOrderForCaller(id, callerId, isAdmin);

        OrderStatus currentOrderStatus = order.getStatus();
        OrderStatus nextOrderStatus = request.orderStatus();

        if (currentOrderStatus == nextOrderStatus) {
            return new OrderResponse(order);
        }

        // CLIENT só pode cancelar o próprio pedido enquanto ele ainda está PENDING.
        // O restante do fluxo (PREPARING, READY, DELIVERED) é do ADMIN.
        if (!isAdmin && (nextOrderStatus != OrderStatus.CANCELLED
                || currentOrderStatus != OrderStatus.PENDING)) {
            throw new BadRequest("Customers can only cancel orders that are still pending");
        }

        // ADMIN só avança um status por vez ou cancela pedidos ainda ativos.
        // CANCELLED e DELIVERED são estados finais e não podem ser alterados.
        if (isAdmin && !currentOrderStatus.canTransitionTo(nextOrderStatus)) {
            throw new BadRequest("Invalid transition: " + currentOrderStatus + " -> " + nextOrderStatus);
        }

        if (nextOrderStatus == OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        order.setStatus(nextOrderStatus);
        return new OrderResponse(order);
    }

    // Rota restrita a ADMIN no SecurityConfig
    @Transactional
    public void delete(Long id) {
        Order order = findOrderById(id);

        // Pedido ainda em andamento tem estoque reservado: devolve antes de apagar
        if (order.getStatus() != OrderStatus.CANCELLED && order.getStatus() != OrderStatus.DELIVERED) {
            restoreStock(order);
        }
        orderRepository.delete(order);
    }

    private void restoreStock(Order order) {
        order.getItems().forEach(i -> i.getProduct().addStock(i.getQuantity()));
    }

    // Se o pedido é de outro usuário, responde 404 para não revelar que ele existe
    private Order findOrderForCaller(Long id, Long callerId, boolean isAdmin) {
        Order order = findOrderById(id);
        if (!isAdmin && !order.getUser().getUserId().equals(callerId)) {
            throw new ResourceNotFound(id);
        }
        return order;
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFound(id));
    }
}