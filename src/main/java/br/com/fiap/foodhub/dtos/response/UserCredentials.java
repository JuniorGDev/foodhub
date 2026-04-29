package br.com.fiap.foodhub.dtos.response;

import br.com.fiap.foodhub.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais internas do usuário", hidden = true)
public record UserCredentials(

        @Schema(hidden = true)
        Long id,

        @Schema(hidden = true)
        String email,

        @Schema(hidden = true)
        String passwordHash,

        @Schema(hidden = true)
        UserType userType
) {
}
