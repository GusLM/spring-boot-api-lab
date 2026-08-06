package com.gustavosantos.library_api.controller.dto.bookgenre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookGenreRequestDTO(
        @NotBlank(message = "required field")
        @Size(min = 2, max = 45, message = "It must be more than 1 character and have a maximum of 45 characters")
        String genre
) {
}
