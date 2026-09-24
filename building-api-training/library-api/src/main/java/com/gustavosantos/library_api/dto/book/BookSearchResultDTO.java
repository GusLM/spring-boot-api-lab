package com.gustavosantos.library_api.dto.book;

import com.gustavosantos.library_api.dto.author.AuthorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(name = "BookSearchResult", description = "Book data returned by search and lookup operations")
public record BookSearchResultDTO(
        UUID publicId,
        String isbn,
        String title,
        LocalDate publicationDate,
        String genre,
        List<AuthorResponseDTO> authors
) {
}
