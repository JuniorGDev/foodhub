package br.com.fiap.foodhub.foodhub.repositories;

import br.com.fiap.foodhub.foodhub.entities.Address;

import java.util.Optional;

public interface AddressRepository {
    Optional<Address> findById(Long id);
    Address save(Address address);
    Integer update(Address address);
    Integer deleteById(Long id);
}
