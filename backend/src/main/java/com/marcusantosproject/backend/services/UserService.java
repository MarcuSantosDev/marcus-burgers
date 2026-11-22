package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.register.RegisterRequestDTO;
import com.marcusantosproject.backend.dto.register.RegisterResponseDTO;
import com.marcusantosproject.backend.dto.user.UserRequest;
import com.marcusantosproject.backend.dto.user.UserResponse;
import com.marcusantosproject.backend.dto.user.UserUpdateRequest;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.enums.Role;
import com.marcusantosproject.backend.repository.OrderRepository;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.exceptions.BadRequest;
import com.marcusantosproject.backend.services.exceptions.EmailAlreadyExists;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    // Rota restrita a ADMIN no SecurityConfig
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(UserResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id, Long callerId, boolean isAdmin) {
        checkAccess(id, callerId, isAdmin);
        return new UserResponse(findEntity(id));
    }

    // Rota restrita a ADMIN no SecurityConfig
    @Transactional
    public UserResponse createByAdmin(UserRequest request) {
        return new UserResponse(createUser(request.name(), request.email(), request.password(), request.role()));
    }

    // Usuário sem token só tem acesso a esse CREATE
    @Transactional
    public RegisterResponseDTO registerClient(RegisterRequestDTO request) {
        User user = createUser(request.name(), request.email(), request.password(), Role.CLIENT);
        return new RegisterResponseDTO(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request, Long callerId, boolean isAdmin) {
        checkAccess(id, callerId, isAdmin);
        User entity = findEntity(id);
        String email = request.email().trim().toLowerCase();

        if (!email.equals(entity.getEmail()) && repository.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }

        entity.setName(request.name());
        entity.setEmail(email);

        // Senha em branco significa "não alterar"
        if (request.password() != null && !request.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.password()));
        }

        return new UserResponse(repository.save(entity));
    }

    // Rota restrita a ADMIN no SecurityConfig
    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFound(id);
        }
        if (orderRepository.existsByUserUserId(id)) {
            throw new BadRequest("Cannot delete user with existing orders");
        }
        repository.deleteById(id);
    }

    private User createUser(String name, String rawEmail, String rawPassword, Role role) {
        String email = rawEmail.trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();

        return repository.save(user);
    }

    // CLIENT só acessa o próprio perfil; para os demais, responde 404
    private void checkAccess(Long id, Long callerId, boolean isAdmin) {
        if (!isAdmin && !id.equals(callerId)) {
            throw new ResourceNotFound(id);
        }
    }

    private User findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }
}