package br.com.fiap.foodhub.repository;

import br.com.fiap.foodhub.domain.Address;
import br.com.fiap.foodhub.dtos.request.AddressRequest;
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
                .sql("SELECT id, street, number, city, zip_code AS zipCode, user_id AS userId FROM address WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> new Address(
                        rs.getLong("id"),
                        rs.getString("street"),
                        rs.getInt("number"),
                        rs.getString("city"),
                        rs.getString("zipCode"),
                        rs.getLong("userId")
                ))
                .optional();
    }

    @Override
    public Integer save(Address address, Long userId) {
        return this.jdbcClient
                .sql("INSERT INTO addresses " +
                        "(street, number, city, zip_code, user_id) " +
                        "VALUES " +
                        "(:street, :number, :city, :zipCode, :userId) ")
                .param("street", address.getStreet())
                .param("number", address.getNumber())
                .param("city", address.getCity())
                .param("zipCode", address.getZipCode())
                .param("userId", userId)
                .update();
    }

    @Override
    public Integer update(AddressRequest addressRequest, Long id) {
        return this.jdbcClient
                .sql("UPDATE addresses a " +
                        "SET " +
                        "street = :street, " +
                        "number = :number, " +
                        "city = :city, " +
                        "zip_code = :zipCode " +
                        "FROM users u " +
                        "WHERE u.id = :userId")
                .param("street", addressRequest.street())
                .param("number", addressRequest.number())
                .param("city", addressRequest.city())
                .param("zipCode", addressRequest.zipCode())
                .param("userId", id)
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
