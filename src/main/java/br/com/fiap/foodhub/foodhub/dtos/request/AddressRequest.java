package br.com.fiap.foodhub.foodhub.dtos.request;

public record AddressRequest(
        String street,
        int number,
        String city,
        String zipCode
) {
}
