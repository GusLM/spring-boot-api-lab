package com.gustavosantos.library_api.controller.dto;

import com.gustavosantos.library_api.model.Author;

import java.time.LocalDate;

public record AuthorDTO(
        String firstName,
        String lastName,
        LocalDate birthDate,
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
