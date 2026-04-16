package br.com.fiap.foodhub.foodhub.repositories;

import br.com.fiap.foodhub.foodhub.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AddressRepositoryImp implements AddressRepository {

    private final JdbcClient jdbcClient;

    public AddressRepositoryImp(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Address> findById(Long id) {
        return this.jdbcClient
                .sql("SELECT id, street, number, city, zip_code AS zipCode FROM address WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> new Address(rs.getLong("id"), rs.getString("street"), rs.getInt("number"), rs.getString("city"), rs.getString("zipCode")))
                .optional();
    }

    @Override
    public Address save(Address address) {
        Long savedAddress = this.jdbcClient
                .sql("INSERT INTO addresses " +
                        "(street, number, city, zip_code) " +
                        "VALUES " +
                        "(:street, :number, :city, :zipCode) " +
                        "RETURNING id")
                .param("street", address.getStreet())
                .param("number", address.getNumber())
                .param("city", address.getCity())
                .param("zipCode", address.getZipCode())
                .query(Long.class)
                .single();

        address.setId(savedAddress);
        return address;
    }

    @Override
    public Integer update(Address address) {
        return this.jdbcClient
                .sql("UPDATE addresses " +
                        "SET " +
                        "street = :street, " +
                        "number = : number, " +
                        "city = :city, " +
                        "zip_code = :zipCode")
                .param("street", address.getStreet())
                .param("number", address.getNumber())
                .param("city", address.getCity())
                .param("zipCode", address.getZipCode())
                .update();
    }

    @Override
    public Integer deleteById(Long id) {
        return this.jdbcClient
                .sql("DELETE FROM addresses WHERE id = :id")
                .param("id", id)
                .update();
    }
}
