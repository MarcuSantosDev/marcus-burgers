package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.user.UserRequest;
import com.marcusantosproject.backend.dto.user.UserResponse;
import com.marcusantosproject.backend.dto.user.UserUpdateRequest;
import com.marcusantosproject.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        List<UserResponse> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponse> createByAdmin(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.createByAdmin(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.userId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UserUpdateRequest request, @PathVariable Long id){
        UserResponse  response = userService.update(id,request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
