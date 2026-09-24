package com.gustavosantos.library_api.dto.author;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "AuthorResponse", description = "Author data returned by the API")
public record AuthorResponseDTO(
        UUID publicId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String nationality) {
}
