package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.address.AddressRequest;
import com.marcusantosproject.backend.dto.address.AddressResponse;
import com.marcusantosproject.backend.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressResource {

    @Autowired
    private AddressService service;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll(){
        List<AddressResponse> response = service.findAll();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id){
        AddressResponse response = service.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public  ResponseEntity<AddressResponse> insert(@Valid @RequestBody AddressRequest request){
        AddressResponse response = service.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.addressId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,@Valid @RequestBody AddressRequest request){
        return ResponseEntity.ok().body(service.update(id,request));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
