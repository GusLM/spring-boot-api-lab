package com.gustavosantos.library_api.controller.dto.book;

import com.gustavosantos.library_api.controller.dto.author.AuthorRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookSearchResultDTO(
        UUID uuid,
        String isbn,
        String title,
        LocalDate publicationDate,
        UUID genrePublicId,
        List<AuthorRequestDTO> authors
) {
}
