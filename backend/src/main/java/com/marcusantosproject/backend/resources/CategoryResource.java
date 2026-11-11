package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.CategoryDTO;
import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.util.List;

@RestController
public class CategoryResource {
    @Autowired
    private CategoryService service;

    @GetMapping(value = "/categories")
    public ResponseEntity<List<Category>> findAll(){
        List<Category> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/categories/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Category obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<Void> insert(@RequestBody CategoryDTO objDto){
        Category obj = service.fromDto(objDto);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getCategoryId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/categories/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id , @RequestBody CategoryDTO objDto){
        Category obj = service.fromDto(objDto);
        obj = service.update(id,obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/categories/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
