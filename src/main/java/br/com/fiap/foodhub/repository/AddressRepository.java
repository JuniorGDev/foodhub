package br.com.fiap.foodhub.repository;

import br.com.fiap.foodhub.domain.Address;
import br.com.fiap.foodhub.dtos.request.AddressRequest;

import java.util.Optional;

public interface AddressRepository {
    Optional<Address> findById(Long id);
    Address save(Address address);
    Integer update(AddressRequest addressRequest, Long id);
    Integer deleteById(Long id);
}
