package br.com.fiap.foodhub.foodhub.dtos.response;

public record AddressResponse(
        String street,
        int number,
        String city,
        String zipCode
) {
}
