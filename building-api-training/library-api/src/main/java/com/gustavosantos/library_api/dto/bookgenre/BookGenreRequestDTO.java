package com.gustavosantos.library_api.dto.bookgenre;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "BookGenreRequest", description = "Data required to create or update a book genre")
public record BookGenreRequestDTO(
        @NotBlank(message = "required field")
        @Size(min = 2, max = 45, message = "It must be more than 1 character and have a maximum of 45 characters")
        String genre
) {
}
