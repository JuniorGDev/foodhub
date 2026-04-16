package br.com.fiap.foodhub.foodhub.dtos.error;

import java.util.List;

public record ValidationErrorResponse(List<String> errors, int statusCode) {
}
