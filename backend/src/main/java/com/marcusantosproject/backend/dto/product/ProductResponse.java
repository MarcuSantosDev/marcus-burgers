package com.marcusantosproject.backend.dto.product;

import com.marcusantosproject.backend.dto.category.CategoryResponse;
import com.marcusantosproject.backend.entities.Product;

import java.math.BigDecimal;

public record ProductResponse(Long productId, String name, String description, BigDecimal price, int stock, CategoryResponse category) {

    public ProductResponse(Product obj){
        this(obj.getProductId(), obj.getName(), obj.getDescription(), obj.getPrice(), obj.getStock(), new CategoryResponse(obj.getCategory()));
    }
}
