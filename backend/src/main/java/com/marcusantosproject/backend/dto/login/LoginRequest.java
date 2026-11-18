package com.marcusantosproject.backend.dto.login;

public record LoginRequest(
        String email,
        String password
) {
}