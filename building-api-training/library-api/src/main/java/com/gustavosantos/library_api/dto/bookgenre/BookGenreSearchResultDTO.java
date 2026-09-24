package com.gustavosantos.library_api.dto.bookgenre;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "BookGenreSearchResult", description = "Book genre data returned by search and lookup operations")
public record BookGenreSearchResultDTO(
        UUID publicId,
        String genre
) {
}
