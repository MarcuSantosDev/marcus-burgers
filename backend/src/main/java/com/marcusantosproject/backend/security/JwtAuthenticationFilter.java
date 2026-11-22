package com.marcusantosproject.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder,
                                   JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                // Valida assinatura e expiração; lança JwtException se algo estiver errado
                Jwt jwt = jwtDecoder.decode(token);

                // O converter (JwtConfig) lê o claim "role" e monta ROLE_ADMIN / ROLE_CLIENT.
                // O principal da autenticação passa a ser o próprio Jwt,
                // então @AuthenticationPrincipal Jwt jwt funciona nos controllers.
                AbstractAuthenticationToken authentication = jwtAuthenticationConverter.convert(jwt);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException e) {
                // Token inválido, expirado ou adulterado: segue sem autenticação
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}