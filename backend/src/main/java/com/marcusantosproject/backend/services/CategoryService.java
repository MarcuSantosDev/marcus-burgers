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
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(CategoryResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id){
        return new CategoryResponse(findCategory(id));
    }

    @Transactional
    public CategoryResponse  insert(CategoryRequest request){
        Category entity = new Category(request.name(), request.description());
        return new CategoryResponse(categoryRepository.save(entity));
    }

    @Transactional
    public CategoryResponse update(Long id,CategoryRequest request){
        Category entity = findCategory(id);
        entity.setName(request.name());
        entity.setDescription(request.description());
        return new CategoryResponse(categoryRepository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFound(id);
        }
        if (productRepository.existsByCategoryCategoryId(id)) {
            throw new CategoryHasProducts(id);
        }
        categoryRepository.deleteById(id);
    }


    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }

}
