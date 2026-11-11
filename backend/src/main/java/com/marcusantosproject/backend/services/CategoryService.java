package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.CategoryDTO;
import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.repository.CategoryRepository;

import com.marcusantosproject.backend.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    }

    public Category findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Category not found"));
    }

    public Category insert(Category obj){
        return repository.save(obj);
    }

    public Category fromDto(CategoryDTO objDto){
        return new Category(objDto.name(),objDto.description());
    }

    public Category update(Long id,Category obj){
        Category newObj = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Category not found"));
        updateData(newObj,obj);
        return repository.save(newObj);
    }

    public void updateData(Category newObj, Category obj){
        newObj.setName(obj.getName());
        newObj.setDescription(obj.getDescription());
    }
}
