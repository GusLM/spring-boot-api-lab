package com.gustavosantos.library_api.controller.dto.exceptions;

public record FieldValidationError(String field, String error) {
}
