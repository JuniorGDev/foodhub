package br.com.fiap.foodhub.foodhub.dtos.request;

import br.com.fiap.foodhub.foodhub.enums.UserType;

public record UserSearchFilter(
        String fullname,
        String email,
        UserType userType
) {
}
