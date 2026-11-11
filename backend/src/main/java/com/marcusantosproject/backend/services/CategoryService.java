package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.category.CategoryRequest;
import com.marcusantosproject.backend.dto.category.CategoryResponse;
import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.repository.CategoryRepository;

import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.services.exceptions.CategoryHasProducts;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id){
        return new CategoryResponse(findEntity(id));
    }

    @Transactional
    public CategoryResponse  insert(CategoryRequest request){
        Category entity = new Category(request.name(), request.description());
        return new CategoryResponse(repository.save(entity));
    }

    @Transactional
    public CategoryResponse update(Long id,CategoryRequest request){
        Category entity = findEntity(id);
        entity.setName(request.name());
        entity.setDescription(request.description());
        return new CategoryResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFound(id);
        }
        if (productRepository.existsByCategoryCategoryId(id)) {
            throw new CategoryHasProducts(id);
        }
        repository.deleteById(id);
    }


    private Category findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }

}
