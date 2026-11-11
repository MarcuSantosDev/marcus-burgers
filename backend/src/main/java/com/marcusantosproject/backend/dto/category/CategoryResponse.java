package com.marcusantosproject.backend.dto.category;

import com.marcusantosproject.backend.entities.Category;

public record CategoryResponse(
        Long categoryId,
        String name,
        String description
) {
    public CategoryResponse(Category obj) {
        this(obj.getCategoryId(), obj.getName(), obj.getDescription());
    }
}
