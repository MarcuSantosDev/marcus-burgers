package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.user.UserRequest;
import com.marcusantosproject.backend.dto.user.UserResponse;
import com.marcusantosproject.backend.dto.user.UserUpdateRequest;
import com.marcusantosproject.backend.security.AuthUtils;
import com.marcusantosproject.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id,
                                                 @AuthenticationPrincipal Jwt jwt) {
        UserResponse response = userService.findById(id, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt));
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createByAdmin(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createByAdmin(request);
        // fromCurrentRequest() geraria /users/create/{id}; aqui montamos o caminho correto
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{id}").buildAndExpand(response.userId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserUpdateRequest request,
                                               @AuthenticationPrincipal Jwt jwt) {
        UserResponse response = userService.update(id, request, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt));
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}