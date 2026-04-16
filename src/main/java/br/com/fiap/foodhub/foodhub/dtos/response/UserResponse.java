package br.com.fiap.foodhub.foodhub.dtos.response;

import br.com.fiap.foodhub.foodhub.enums.UserType;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String fullname,
        String email,
        UserType userType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AddressResponse address
) {
}
