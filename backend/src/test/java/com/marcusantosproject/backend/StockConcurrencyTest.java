package com.marcusantosproject.backend;

import com.marcusantosproject.backend.dto.OrderItem.OrderItemRequest;
import com.marcusantosproject.backend.dto.order.OrderRequest;
import com.marcusantosproject.backend.entities.Address;
import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.enums.Role;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.repository.CategoryRepository;
import com.marcusantosproject.backend.repository.OrderRepository;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.OrderService;
import com.marcusantosproject.backend.services.exceptions.InsufficientStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class StockConcurrencyTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Long productId;
    private Long firstAddressId;
    private Long secondAddressId;
    private Long firstUserId;
    private Long secondUserId;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = categoryRepository.save(new Category("Hambúrgueres", "Lanches"));

        Product product = productRepository.saveAndFlush(
                new Product("Hambúrguer artesanal", "Teste", null,
                        new BigDecimal("25.00"), 1, category));
        productId = product.getProductId();

        User firstUser = userRepository.save(new User(
                "Cliente 1", "cliente1@test.com", "senha123", Role.CLIENT));
        User secondUser = userRepository.save(new User(
                "Cliente 2", "cliente2@test.com", "senha123", Role.CLIENT));
        firstUserId = firstUser.getUserId();
        secondUserId = secondUser.getUserId();

        firstAddressId = addressRepository.save(new Address(
                "Casa", "Rua 1", "10", "", "Centro", "João Pessoa", "PB",
                "58000000", firstUser)).getAddressId();
        secondAddressId = addressRepository.save(new Address(
                "Casa", "Rua 2", "20", "", "Centro", "João Pessoa", "PB",
                "58000000", secondUser)).getAddressId();
    }

    @Test
    void shouldAllowOnlyOneOrderWhenStockIsOne() throws Exception {
        int numberOfOrders = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfOrders);
        CountDownLatch ready = new CountDownLatch(numberOfOrders);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<Boolean> first = executor.submit(() -> createOrder(
                    firstUserId, firstAddressId, ready, start));
            Future<Boolean> second = executor.submit(() -> createOrder(
                    secondUserId, secondAddressId, ready, start));

            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();

            int successfulOrders = (first.get(10, TimeUnit.SECONDS) ? 1 : 0)
                    + (second.get(10, TimeUnit.SECONDS) ? 1 : 0);

            assertEquals(1, successfulOrders);

            Product updatedProduct = productRepository.findById(productId).orElseThrow();
            assertEquals(0, updatedProduct.getStock());
            assertEquals(1, orderRepository.count());
        } finally {
            executor.shutdownNow();
        }
    }

    private Boolean createOrder(Long userId, Long addressId,
            CountDownLatch ready, CountDownLatch start) {
        ready.countDown();
        try {
            start.await();
            OrderRequest request = new OrderRequest(addressId,
                    List.of(new OrderItemRequest(productId, 1)));
            orderService.insert(userId, false, request);
            return true;
        } catch (InsufficientStock | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }
}
