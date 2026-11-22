package com.marcusantosproject.backend.dto.register;

import com.marcusantosproject.backend.entities.User;

public record RegisterResponseDTO(
        String name,
        String email
) {
    public RegisterResponseDTO(User user) {
        this(user.getName(), user.getEmail());
    }
}