package com.marcusantosproject.backend.security;

 import com.marcusantosproject.backend.dto.login.LoginRequest;
import com.marcusantosproject.backend.dto.login.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        /*
         O AuthenticationManager recebe o token com as credenciais do usuário e delega o processo de autenticação ao AuthenticationProvider apropriado :
         - AuthenticationManager: coordena a autenticação
         - AuthenticationProvider: realiza a validação das credenciais
         - UserDetailsService: busca os dados do usuário.
         - PasswordEncoder: verifica a senha que valida as credenciais e retorna um Authentication autenticado
        */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        String token = jwtService.generateToken(
                authentication.getName(),
                role
        );

        return new LoginResponse(token);
    }
}