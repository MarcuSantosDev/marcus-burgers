package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.product.ProductRequest;
import com.marcusantosproject.backend.dto.product.ProductResponse;
import com.marcusantosproject.backend.dto.product.StockRequest;
import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.repository.CategoryRepository;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.services.exceptions.CategoryNotFound;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService{
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(){return repository.findAll().stream().map(ProductResponse::new).toList();}

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id){
        Product entity = findEntity(id);
        return new ProductResponse(entity);
    }

    @Transactional
    public ProductResponse insert(ProductRequest request){
        Category category =  findEntityCategory(request);
        Product entity = new Product(request.name(),request.description(),request.price(),request.stock(),category);
        return new ProductResponse(repository.save(entity));
    }

    @Transactional
    public ProductResponse update(Long id,ProductRequest request){
        Category category =  findEntityCategory(request);
        Product entity = findEntity(id);
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setCategory(category);
        return new ProductResponse(repository.save(entity));

    }

    @Transactional
    public void deleteById(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFound(id);
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
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }

    private Category findEntityCategory(ProductRequest request){
        return categoryRepository.findById(request.categoryId()).orElseThrow(() -> new CategoryNotFound(request.categoryId()));
    }

}
