package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.address.AddressRequest;
import com.marcusantosproject.backend.dto.address.AddressResponse;
import com.marcusantosproject.backend.entities.Address;
import com.marcusantosproject.backend.entities.User;
import com.marcusantosproject.backend.enums.OrderStatus;
import com.marcusantosproject.backend.repository.AddressRepository;
import com.marcusantosproject.backend.repository.OrderRepository;
import com.marcusantosproject.backend.repository.UserRepository;
import com.marcusantosproject.backend.services.exceptions.BadRequest;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<AddressResponse> findAll() {
        return addressRepository.findByActiveTrue()
                .stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id) {
        return new AddressResponse(findAddressById(id));
    }

    @Transactional
    public AddressResponse insert(AddressRequest request) {
        User user = findUserById(request.userId());
        Address address = new Address(
                request.name(),
                request.street(),
                request.number(),
                request.complement(),
                request.neighborhood(),
                request.city(),
                request.state(),
                request.zipCode(),
                user
        );
        return new AddressResponse(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse update(Long id, AddressRequest request) {
        Address entity = findAddressById(id);
        entity.setName(request.name());
        entity.setStreet(request.street());
        entity.setNumber(request.number());
        entity.setComplement(request.complement());
        entity.setNeighborhood(request.neighborhood());
        entity.setCity(request.city());
        entity.setState(request.state());
        entity.setZipCode(request.zipCode());
        return new AddressResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Address address = findAddressById(id);

        // Pedidos que ainda dependem do endereço
        List<OrderStatus> blockingStatuses = List.of(
                OrderStatus.PENDING,
                OrderStatus.PREPARING
        );

        if (orderRepository.existsByAddress_AddressIdAndStatusIn(id, blockingStatuses)) {
            throw new BadRequest("Cannot delete address with orders pending or being prepared");
        }

        // Soft delete: o histórico dos pedidos continua íntegro
        address.setActive(false);
    }

    private Address findAddressById(Long id) {
        return addressRepository.findByAddressIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Usuario com id [" + id + "] não encontrado"
                ));
    }
}