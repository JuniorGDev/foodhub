package br.com.fiap.foodhub.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para alteração de senha")
public record UserCredentialsRequest(

        @Schema(
                description = "Senha atual do usuário",
                example = "SenhaAtual123"
        )
        @NotBlank(message = "A senha atual é obrigatória")
        String currentPassword,

        @Schema(
                description = "Nova senha do usuário",
                example = "NovaSenha123"
        )
        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres")
        String newPassword

) {
}
