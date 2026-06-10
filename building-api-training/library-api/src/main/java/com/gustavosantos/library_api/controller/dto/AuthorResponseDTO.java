package com.gustavosantos.library_api.controller.dto;

import com.gustavosantos.library_api.model.Author;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID publicId,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String nationality) {

    public Author toEntity() {
        return new Author(
                this.firstName,
                this.lastName,
                this.birthDate,
                this.nationality
        );
    }

    public AuthorResponseDTO toAuthorResponseDTO(Author author) {
        return new AuthorResponseDTO(
                author.getPublicId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );
    }
}
