package br.com.fiap.foodhub.dtos.request;

import br.com.fiap.foodhub.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros para pesquisa de usuários")
public record UserSearchFilter(

        @Schema(
                description = "Nome completo ou parte do nome do usuário",
                example = "John Doe"
        )
        String fullname,

        @Schema(
                description = "E-mail do usuário",
                example = "joe.doe@email.com"
        )
        String email,

        @Schema(
                description = "Tipo do usuário",
                example = "ADMIN"
        )
        UserType userType

) {
}
