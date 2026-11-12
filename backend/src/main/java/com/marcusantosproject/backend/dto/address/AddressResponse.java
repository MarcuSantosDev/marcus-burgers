package com.marcusantosproject.backend.dto.address;

import com.marcusantosproject.backend.entities.Address;

public record AddressResponse(
        Long addressId,
        String name,
        String street,
        Integer number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipcode
) {

    public AddressResponse(Address addr) {
        this(
                addr.getAddressId(),
                addr.getName(),
                addr.getStreet(),
                addr.getNumber(),
                addr.getComplement(),
                addr.getNeighborhood(),
                addr.getCity(),
                addr.getState(),
                addr.getZipcode()
        );
    }
}