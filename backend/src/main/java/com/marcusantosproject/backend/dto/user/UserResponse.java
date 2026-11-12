package com.marcusantosproject.backend.dto.user;

import com.marcusantosproject.backend.entities.User;

public record UserResponse(Long userID, String name, String email) {
    public UserResponse(User obj){
        this(obj.getUserId(), obj.getName(), obj.getEmail());
    }
}
