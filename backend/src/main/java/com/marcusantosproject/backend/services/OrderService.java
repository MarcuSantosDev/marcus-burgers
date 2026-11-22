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

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(){
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id){
        return new OrderResponse(findOrderById(id));
    }

    @Transactional
    public OrderResponse insert(OrderRequest request, String email){
        // Buscar Usuário
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound(
                        "User not found with email: " + email));
        // Buscar o endereço e verifica se o endereço realmente pertence ao usuário autenticado
        Address addressEntity = addressRepository
                .findByAddressIdAndUserUserId(request.addressId(), userEntity.getUserId())
                .orElseThrow(() -> new ResourceNotFound(
                        "Address not found or does not belong to the authenticated user."));

        Order orderEntity = Order.builder()
                .user(userEntity)
                .address(addressEntity)
                .build();

        for (OrderItemRequest itemRequest : request.items()) {
            // Buscar o produto pelo ID.
            Product product = findProductById(itemRequest.productId());
            // Remove o item do estoque
            product.removeStock(itemRequest.quantity());
            // Criar OrderItem utilizando Builder
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            // Adicionar o item ao pedido.
            orderEntity.addItem(orderItem);
        }

        // Salva o pedido no banco
        return new OrderResponse(orderRepository.save(orderEntity));
    }

    @Transactional
    public OrderResponse update(Long id, OrderUpdate request) {
        // Busca o pedido no banco
        Order order = findOrderById(id);

        // Status atual do pedido
        OrderStatus currentOrderStatus = order.getStatus();
        // Status que o cliente da API está pedindo para aplicar
        OrderStatus nextOrderStatus = request.orderStatus();

        if (currentOrderStatus == nextOrderStatus) {
            return new OrderResponse(order);
        }

        // Pergunta ao enum se essa mudança é permitida
        if (!currentOrderStatus.canTransitionTo(nextOrderStatus)) {
            throw new IllegalStateException("Transição inválida: " + currentOrderStatus + " -> " + nextOrderStatus);
        }

        if (nextOrderStatus == OrderStatus.CANCELLED) {
            order.getItems().forEach(i -> i.getProduct().addStock(i.getQuantity()));
        }

        order.setStatus(nextOrderStatus);
        return new OrderResponse(order);
    }

    @Transactional
    public void delete(Long id){
        if(!orderRepository.existsById(id)){
            throw new ResourceNotFound(id);
        }
        orderRepository.deleteById(id);
    }

    private Order findOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }
    
    private Product findProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }
    

}
