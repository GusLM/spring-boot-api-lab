package com.gustavosantos.library_api.controller.dto.book;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookCreateRequestDTO(
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

    public BookCreateRequestDTO toBookDTO(Book book) {
        List<UUID> authorsId = book.getAuthors().stream().map(Author::getPublicId).toList();

        return new BookCreateRequestDTO(
                book.getIsbn(),
                book.getTitle(),
                book.getPublicationDate(),
                book.getGenre().getPublicId(),
                authorsId
        );
    }
}
