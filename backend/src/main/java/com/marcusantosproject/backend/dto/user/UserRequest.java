package com.marcusantosproject.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(max = 100)
        String name,
        @NotBlank @Email @Size(max = 255)
        String email,
        @NotBlank @Size(min = 8, max = 72, message = "Password must have between 8 and 72 characters")
        String password)
{}
