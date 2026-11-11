package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.entities.Product;
import com.marcusantosproject.backend.repository.ProductRepository;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class ProductService implements Serializable {
    @Autowired
    private ProductRepository repository;

    public List<Product> findAll(){return repository.findAll();}

    public Product findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

}
