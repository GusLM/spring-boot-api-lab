package com.gustavosantos.library_api.controller.dto.book;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookCreateDTO(
        @ISBN
        @NotBlank(message = "required field")
        @Size(min = 5, max = 20, message = "It must be more than 4 character and have a maximum of 20 characters")
        String isbn,

        @NotBlank(message = "required field")
        @Size(min = 2, max = 200, message = "It must be more than 1 character and have a maximum of 200 characters")
        String title,

        @NotNull(message = "required field")
        @Past(message = "It cannot be a future date")
        LocalDate publicationDate,

        UUID genrePublicId,

        List<UUID> authorsPublicIds
) {
}
