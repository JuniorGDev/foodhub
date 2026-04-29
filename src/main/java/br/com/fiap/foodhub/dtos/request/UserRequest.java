package br.com.fiap.foodhub.dtos.request;

import br.com.fiap.foodhub.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de usuário")
public record UserRequest(

        @Schema(description = "Nome completo do usuário", example = "Geová José da Silva Junior")
        @NotBlank(message = "O nome completo é obrigatório")
        String fullname,

        @Schema(description = "E-mail do usuário", example = "geova@email.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail informado é inválido")
        String email,

        @Schema(description = "Senha do usuário", example = "Senha123")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @Schema(description = "Tipo de usuário", example = "CLIENT")
        @NotNull(message = "O tipo de usuário é obrigatório")
        UserType userType,

        @Schema(description = "Endereço do usuário")
        @NotNull(message = "O endereço é obrigatório")
        @Valid
        AddressRequest address
) {
}
