package com.marcusantosproject.backend.security;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(String email, String role) {

        Instant now = Instant.now();
        // As Claims são informações armazenadas dentro do payload do JWT.
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("MarcusBurguer") // iss: quem emitiu
                .subject(email) // sub: de quem é o token
                .issuedAt(now) // iat: quando foi criado
                .expiresAt(now.plusSeconds(3600)) // exp: expira em 1h
                .claim("role", role) // claim customizado
                .build();

        // O Header contém informações sobre como o JWT será assinado
        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        // Codificar ambos em Base64URL. Assina o conteúdo utilizando HS256 e sua SecretKey
        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue(); // Obtém o JWT completo em formato String
    }
}