package com.gustavosantos.library_api.controller.dto.author;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID publicId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String nationality) {
}
