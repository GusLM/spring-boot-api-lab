package com.gustavosantos.library_api.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.ISBN;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(name = "BookRequest", description = "Data required to create or update a book")
public record BookRequestDTO(
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

        @NotEmpty(message = "empty list not allowed")
        List<UUID> authorsPublicIds
) {
}
