package com.marcusantosproject.backend.security;

import org.springframework.security.oauth2.jwt.Jwt;

public final class AuthUtils {

    private AuthUtils() {}

    public static Long userId(Jwt jwt) {
        // O claim pode voltar como Long ou Integer dependendo da serialização
        return ((Number) jwt.getClaim("userId")).longValue();
    }

    public static boolean isAdmin(Jwt jwt) {
        return "ADMIN".equals(jwt.getClaimAsString("role"));
    }
}