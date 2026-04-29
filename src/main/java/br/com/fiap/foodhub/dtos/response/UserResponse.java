package br.com.fiap.foodhub.dtos.response;

import br.com.fiap.foodhub.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Informações do usuário")
public record UserResponse(
        @Schema(
                description = "ID do usuário",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Nome completo do usuário",
                example = "John Doe"
        )
        String fullname,
        @Schema(
                description = "Email do usuário",
                example = "john.doe@example.com"
        )
        String email,
        @Schema(
                description = "Tipo do usuário",
                example = "CLIENT"
        )
        UserType userType,
        @Schema(
                description = "Data de criação do usuário",
                example = "2023-01-01T00:00:00"
        )
        LocalDateTime createdAt,
        @Schema(
                description = "Data de atualização do usuário",
                example = "2023-01-01T00:00:00"
        )
        LocalDateTime updatedAt,
        @Schema(
                description = "Endereço do usuário",
                example = "123 Main St, New York, NY 10001"
        )
        AddressResponse address
) {
}
