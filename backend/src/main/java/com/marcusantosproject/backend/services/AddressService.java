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

    // ADMIN vê todos os endereços ativos; CLIENT vê só os seus
    @Transactional(readOnly = true)
    public List<AddressResponse> findAll(Long callerId, boolean isAdmin) {
        List<Address> addresses = isAdmin
                ? addressRepository.findByActiveTrue()
                : addressRepository.findByUserUserIdAndActiveTrue(callerId);
        return addresses.stream().map(AddressResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id, Long callerId, boolean isAdmin) {
        return new AddressResponse(findAddressForCaller(id, callerId, isAdmin));
    }

    // O dono do endereço é sempre quem está logado (callerId vem do token, nunca do body)
    @Transactional
    public AddressResponse insert(Long callerId, AddressRequest request) {
        User user = findUserById(callerId);
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
    public AddressResponse update(Long id, AddressRequest request, Long callerId, boolean isAdmin) {
        Address entity = findAddressForCaller(id, callerId, isAdmin);
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
    public void delete(Long id, Long callerId, boolean isAdmin) {
        Address address = findAddressForCaller(id, callerId, isAdmin);

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

    // Se o endereço é de outro usuário, responde 404 para não revelar que ele existe
    private Address findAddressForCaller(Long id, Long callerId, boolean isAdmin) {
        Address address = findAddressById(id);
        if (!isAdmin && !address.getUser().getUserId().equals(callerId)) {
            throw new ResourceNotFound(id);
        }
        return address;
    }

    private Address findAddressById(Long id) {
        return addressRepository.findByAddressIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found. Id: " + id));
    }
}