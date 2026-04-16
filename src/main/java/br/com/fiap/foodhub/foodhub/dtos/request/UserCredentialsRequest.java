package br.com.fiap.foodhub.foodhub.dtos.request;

public record UserCredentialsRequest(
        Long id,
        String currentPassword,
        String newPassword
) {
}
