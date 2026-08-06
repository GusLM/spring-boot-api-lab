package com.gustavosantos.library_api.controller.dto.bookgenre;

import java.util.UUID;

public record BookGenreSearchResultDTO(
        UUID publicId,
        String genre
) {
}
