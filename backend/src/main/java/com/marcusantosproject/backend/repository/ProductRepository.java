package com.marcusantosproject.backend.repository;

import com.marcusantosproject.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategoryCategoryId(Long categoryId);
}
