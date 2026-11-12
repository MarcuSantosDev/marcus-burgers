package com.marcusantosproject.backend.resources;


import com.marcusantosproject.backend.dto.request.LoginRequest;
import com.marcusantosproject.backend.dto.login.LoginResponse;
import com.marcusantosproject.backend.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
