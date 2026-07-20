package com.gustavosantos.library_api.controller.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorDTO(
        @NotBlank(message = "required field")
        @Size(max = 100, min = 2, message = "It must be more than 1 character and have a maximum of 100 characters")
        String firstName,

        @NotBlank(message = "required field")
        @Size(max = 100, min = 2, message = "It must be more than 1 character and have a maximum of 100 characters")
        String lastName,

        @NotNull(message = "required field")
        @Past(message = "It cannot be a future date")
        LocalDate birthDate,

        @NotBlank(message = "required field")
        @Size(max = 100, min = 2, message = "It must be more than 1 character and have a maximum of 100 characters")
        String nationality) {
}
