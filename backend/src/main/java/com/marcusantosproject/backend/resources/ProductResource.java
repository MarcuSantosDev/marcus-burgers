package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.product.ProductRequest;
import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductResource {
    @Autowired
    private ProductService service;

    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductRequest>> findAll(){
        List<ProductRequest> list = service.findAll().stream().map(ProductRequest::new).toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<ProductRequest> findById(@PathVariable Long id){
        Product obj = service.findById(id);
        return ResponseEntity.ok().body(new ProductRequest(obj));
    }
}
