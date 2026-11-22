package com.marcusantosproject.backend.dto.category;

import com.marcusantosproject.backend.entities.Category;

public record CategoryResponse(
        Long categoryId,
        String name
) {
    public CategoryResponse(Category obj) {
        this(obj.getCategoryId(), obj.getName());
    }
}
