package br.com.fiap.foodhub.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações de endereço do usuário")
public record AddressResponse(

        @Schema(
                description = "Rua do endereço",
                example = "Rua das Flores"
        )
        String street,

        @Schema(
                description = "Número do endereço",
                example = "123"
        )
        int number,

        @Schema(
                description = "Cidade do endereço",
                example = "São Paulo"
        )
        String city,

        @Schema(
                description = "CEP do endereço",
                example = "01234-567"
        )
        String zipCode
) {
}
