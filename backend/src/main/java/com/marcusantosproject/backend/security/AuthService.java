package com.marcusantosproject.backend.security;

import com.marcusantosproject.backend.dto.login.LoginRequest;
import com.marcusantosproject.backend.dto.login.LoginResponse;
import com.marcusantosproject.backend.dto.register.RegisterRequestDTO;
import com.marcusantosproject.backend.dto.register.RegisterResponseDTO;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        /*
         O AuthenticationManager delega ao AuthenticationProvider:
         - UserDetailsService busca o usuário
         - PasswordEncoder confere a senha
         Se as credenciais forem inválidas, lança AuthenticationException.
        */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password())
        );

        // Busca a entidade para colocar userId e role no token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new LoginResponse(jwtService.generateToken(user));
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {
        return userService.registerClient(request);
    }
}