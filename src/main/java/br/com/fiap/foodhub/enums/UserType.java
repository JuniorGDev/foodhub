package br.com.fiap.foodhub.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de usuário do sistema")
public enum UserType {
    CLIENT,
    OWNER,
    ADMIN
}
