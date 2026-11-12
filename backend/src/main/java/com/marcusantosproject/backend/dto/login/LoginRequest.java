package com.marcusantosproject.backend.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}