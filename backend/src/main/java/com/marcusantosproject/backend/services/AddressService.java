package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.address.AddressRequest;
import com.marcusantosproject.backend.dto.address.AddressResponse;
import com.marcusantosproject.backend.entities.Address;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.services.exceptions.AddressNotFound;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;

    @Transactional(readOnly = true)
    public List<AddressResponse> findAll(){
        List<AddressResponse> list = repository.findAll().stream().map(AddressResponse::new).toList();
        return list;
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id){
        return new AddressResponse(findEntity(id));
    }

    @Transactional
    public AddressResponse insert(AddressRequest request){
        Address address = new Address(request.name(),request.street(),request.number(),request.complement(),request.neighborhood(),request.city(),request.state(),request.zipcode());
        return new AddressResponse(repository.save(address));

    }

    private Address findEntity(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFound(id));
    }
}
