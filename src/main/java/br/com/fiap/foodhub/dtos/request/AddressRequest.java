package br.com.fiap.foodhub.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados de endereço do usuário")
public record AddressRequest(

        @Schema(
                description = "Rua do endereço",
                example = "Rua das Flores"
        )
        @NotBlank(message = "A rua é obrigatória")
        String street,

        @Schema(
                description = "Número do endereço",
                example = "123"
        )
        @NotNull(message = "O número é obrigatório")
        @Positive(message = "O número deve ser maior que zero")
        Integer number,

        @Schema(
                description = "Cidade do endereço",
                example = "São Paulo"
        )
        @NotBlank(message = "A cidade é obrigatória")
        String city,

        @Schema(
                description = "CEP do endereço",
                example = "01234-567"
        )
        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(
                regexp = "^\\d{5}-?\\d{3}$",
                message = "CEP inválido"
        )
        String zipCode
) {
}
