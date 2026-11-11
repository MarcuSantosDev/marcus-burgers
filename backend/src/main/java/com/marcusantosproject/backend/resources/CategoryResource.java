package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.category.CategoryRequest;
import com.marcusantosproject.backend.dto.category.CategoryResponse;
import com.marcusantosproject.backend.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CategoryResource {
    @Autowired
    private CategoryService service;

    @GetMapping(value = "/categories")
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(value = "/categories/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id){
        CategoryResponse obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<CategoryResponse> insert(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse obj = service.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.categoryId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping(value = "/categories/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
