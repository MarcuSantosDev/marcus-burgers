package com.marcusantosproject.backend.dto;

import com.marcusantosproject.backend.entities.Category;

public record CategoryDTO(
        String name,
        String description
) {

    public CategoryDTO(Category obj) {
        this(
                obj.getName(),
                obj.getDescription()
        );
    }
}