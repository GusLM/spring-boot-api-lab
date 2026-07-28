package com.gustavosantos.library_api.controller.dto.book;

import com.gustavosantos.library_api.controller.dto.author.AuthorResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookSearchResultDTO(
        UUID publicId,
        String isbn,
        String title,
        LocalDate publicationDate,
        String genre,
        List<AuthorResponseDTO> authors
) {
}
