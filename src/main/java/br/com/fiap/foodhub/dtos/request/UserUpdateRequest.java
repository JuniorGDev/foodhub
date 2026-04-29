package br.com.fiap.foodhub.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de usuário")
public record UserUpdateRequest(

        @Schema(
                description = "Nome completo do usuário",
                example = "Joe Doe"
        )
        @NotBlank(message = "O nome completo é obrigatório")
        String fullname,

        @Schema(
                description = "E-mail do usuário",
                example = "joe.doe@email.com"
        )
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O e-mail informado é inválido")
        String email,

        @Schema(
                description = "Endereço atualizado do usuário"
        )
        @NotNull(message = "O endereço é obrigatório")
        @Valid
        AddressRequest address

) {
}
