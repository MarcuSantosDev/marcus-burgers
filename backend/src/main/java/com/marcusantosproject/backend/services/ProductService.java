package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.product.ProductRequest;
import com.marcusantosproject.backend.dto.product.ProductResponse;
import com.marcusantosproject.backend.dto.product.StockRequest;
import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService{
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(){return repository.findAll().stream().map(ProductResponse::new).toList();}

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id){
        Product entity = findEntity(id);
        return new ProductResponse(entity);
    }

    @Transactional
    public ProductResponse insert(ProductRequest request){
        Product entity = new Product(request.name(),request.description(),request.price(),request.stock());
        return new ProductResponse(repository.save(entity));
    }

    @Transactional
    public ProductResponse update(Long id,ProductRequest request){
        Product entity = findEntity(id);
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        return new ProductResponse(repository.save(entity));

    }

    @Transactional
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException(id);
        }
        repository.deleteById(id);
    }
    @Transactional
    public ProductResponse addStock(Long id, StockRequest request){
        Product entity = findEntity(id);
        entity.addStock(request.quantity());
        return new ProductResponse(entity);
    }

    @Transactional
    public ProductResponse removeStock(Long id, StockRequest request){
        Product entity = findEntity(id);
        entity.removeStock(request.quantity());
        return new ProductResponse(entity);
    }

    private Product findEntity(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

}
