package br.com.fiap.foodhub.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação")
public record LoginResponse(
        @Schema(description = "Token Bearer JWT")
        String token
) {
}
