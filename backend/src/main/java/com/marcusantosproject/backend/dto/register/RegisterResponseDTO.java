package com.marcusantosproject.backend.dto.register;

import com.marcusantosproject.backend.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterResponseDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email )

    {
        public RegisterResponseDTO(User user){
            this(user.getName(), user.getEmail());
        }
    }
