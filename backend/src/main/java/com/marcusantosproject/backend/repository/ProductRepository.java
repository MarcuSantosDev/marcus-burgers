package com.marcusantosproject.backend.repository;

import com.marcusantosproject.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
