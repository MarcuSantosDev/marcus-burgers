package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.user.UserRequest;
import com.marcusantosproject.backend.dto.user.UserResponse;
import com.marcusantosproject.backend.dto.user.UserUpdateRequest;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.exceptions.EmailAlreadyExists;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserResponse insert(UserRequest request){
        String email = request.email().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }
        String hash = passwordEncoder.encode(request.password());
        User entity = new User(request.name(), email, hash);
        return new UserResponse(repository.save(entity));
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
            entity.setPasswordHash(passwordEncoder.encode(request.password()));
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
