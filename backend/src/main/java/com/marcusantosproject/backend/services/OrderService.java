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

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(){
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id){
        return new OrderResponse(findOrderById(id));
    }

    @Transactional
    public OrderResponse insert(OrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFound(request.userId()));

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
            throw new BadRequest("Transição inválida: " + currentOrderStatus + " -> " + nextOrderStatus);
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
