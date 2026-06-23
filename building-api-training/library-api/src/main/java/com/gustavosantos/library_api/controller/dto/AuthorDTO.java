package com.gustavosantos.library_api.controller.dto;

import com.gustavosantos.library_api.model.Author;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthorDTO(
        @NotBlank(message = "required field")
        String firstName,
        @NotBlank(message = "required field")
        String lastName,
        @NotNull(message = "required field")
        LocalDate birthDate,
        @NotBlank(message = "required field")
        String nationality) {

    public Author toEntity(){
        return new Author(
                this.firstName,
                this.lastName,
                this.birthDate,
                this.nationality
        );
    }

    public AuthorDTO toAuthorDTO(Author author) {
        return new AuthorDTO(
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );
    }
}
