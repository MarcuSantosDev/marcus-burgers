package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.address.AddressRequest;
import com.marcusantosproject.backend.dto.address.AddressResponse;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressResource {

     private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll(){
        List<AddressResponse> response = addressService.findAll();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id){
        AddressResponse response = addressService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public  ResponseEntity<AddressResponse> insert(@Valid @RequestBody AddressRequest request){
        AddressResponse response = addressService.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.addressId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,@Valid @RequestBody AddressRequest request){
        return ResponseEntity.ok().body(addressService.update(id,request));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
