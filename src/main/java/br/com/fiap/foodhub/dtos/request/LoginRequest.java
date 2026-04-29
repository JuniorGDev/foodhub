package br.com.fiap.foodhub.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para autenticação do usuário")
public record LoginRequest(

        @Schema(
                description = "E-mail do usuário",
                example = "admin@admin.com"
        )
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail informado é inválido")
        String email,

        @Schema(
                description = "Senha do usuário",
                example = "admin@123"
        )
        @NotBlank(message = "A senha é obrigatória")
        String password

) {
}
