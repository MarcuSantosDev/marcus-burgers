package com.marcusantosproject.backend.resources;

import com.marcusantosproject.backend.dto.address.AddressRequest;
import com.marcusantosproject.backend.dto.address.AddressResponse;
import com.marcusantosproject.backend.dto.address.ViaCepResponse;
import com.marcusantosproject.backend.security.AuthUtils;
import com.marcusantosproject.backend.services.AddressService;
import com.marcusantosproject.backend.services.ViaCepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressResource {

    private final AddressService addressService;
    private final ViaCepService viaCepService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll(@AuthenticationPrincipal Jwt jwt) {
        List<AddressResponse> response = addressService.findAll(AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        AddressResponse response = addressService.findById(id, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<AddressResponse> insert(@Valid @RequestBody AddressRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        AddressResponse response = addressService.insert(AuthUtils.userId(jwt), request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.addressId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,
            @Valid @RequestBody AddressRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok().body(
                addressService.update(id, request, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        addressService.delete(id, AuthUtils.userId(jwt), AuthUtils.isAdmin(jwt));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cep/{zipCode}")
    public ViaCepResponse findByZipCode(@PathVariable @Valid String zipCode) {
        return viaCepService.findByZipCode(zipCode);
    }
}