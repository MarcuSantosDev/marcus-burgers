package com.marcusantosproject.backend.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        // Sem @Size: no login só importa se a senha confere. A regra de tamanho vale no cadastro.
        @NotBlank(message = "Senha é obrigatória")
        String password
) {
}