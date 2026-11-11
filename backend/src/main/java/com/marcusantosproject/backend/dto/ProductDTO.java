package com.marcusantosproject.backend.dto;

import com.marcusantosproject.backend.entities.Product;

import java.math.BigDecimal;

public record ProductDTO(String name, String description, BigDecimal price, int stock) {
    public ProductDTO(Product obj){
        this(
                obj.getName(),
                obj.getDescription(),
                obj.getPrice(),
                obj.getStock()
        );
    }
}
