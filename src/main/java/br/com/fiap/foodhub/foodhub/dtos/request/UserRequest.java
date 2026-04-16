package br.com.fiap.foodhub.foodhub.dtos.request;

import br.com.fiap.foodhub.foodhub.enums.UserType;

public record UserRequest(
        String fullname,
        String email,
        String password,
        UserType userType,
        AddressRequest address
) {
}
