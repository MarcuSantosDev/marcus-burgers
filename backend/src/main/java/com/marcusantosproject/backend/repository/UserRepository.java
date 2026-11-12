package com.marcusantosproject.backend.repository;

import com.marcusantosproject.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByEmail(String email);

    boolean existsByAddressAddressId(Long categoryId);
}
