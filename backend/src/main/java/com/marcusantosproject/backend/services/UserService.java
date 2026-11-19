package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.register.RegisterRequestDTO;
import com.marcusantosproject.backend.dto.register.RegisterResponseDTO;
import com.marcusantosproject.backend.dto.user.UserRequest;
import com.marcusantosproject.backend.dto.user.UserResponse;
import com.marcusantosproject.backend.dto.user.UserUpdateRequest;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.enums.Role;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.exceptions.EmailAlreadyExists;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<UserResponse> findAll(){
        return repository.findAll().stream().map(UserResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id){
        User entity = findEntity(id);
        return new UserResponse(entity);
    }

    @Transactional
    public UserResponse createByAdmin(UserRequest request) {
        return new UserResponse(createUser(request.name(), request.email(), request.password(), request.role()));
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
    // Usuário sem token só tem acesso a esse CREATE
    @Transactional
    public RegisterResponseDTO registerClient(RegisterRequestDTO request) {
        User user = createUser(request.name(), request.email(), request.password(), Role.CLIENT);
        return new RegisterResponseDTO(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User entity = findEntity(id);
        String email = request.email().trim().toLowerCase();

        if (!email.equals(entity.getEmail()) && repository.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }

        entity.setName(request.name());
        entity.setEmail(email);

        if (request.password() != null) {
            entity.setPassword(passwordEncoder.encode(request.password()));
        }
        entity = repository.save(entity);

        return new UserResponse(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFound(id);
        }
        repository.deleteById(id);
    }

    private User findEntity(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }


}
