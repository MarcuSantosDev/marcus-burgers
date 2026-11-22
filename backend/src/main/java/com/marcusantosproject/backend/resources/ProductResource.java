package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.product.ProductRequest;
import com.marcusantosproject.backend.dto.product.ProductResponse;
import com.marcusantosproject.backend.dto.product.StockRequest;
import com.marcusantosproject.backend.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductResource {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> list = productService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse response = productService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> insert(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.productId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock/add")
    public ResponseEntity<ProductResponse> addStock(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        ProductResponse response = productService.addStock(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock/remove")
    public ResponseEntity<ProductResponse> removeStock(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        ProductResponse response = productService.removeStock(id, request);
        return ResponseEntity.ok(response);
    }

}