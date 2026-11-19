package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.login.LoginRequest;
import com.marcusantosproject.backend.dto.login.LoginResponse;
import com.marcusantosproject.backend.dto.register.RegisterRequestDTO;
import com.marcusantosproject.backend.dto.register.RegisterResponseDTO;
import com.marcusantosproject.backend.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest requestDTO) {

        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(requestDTO));
    }
}
