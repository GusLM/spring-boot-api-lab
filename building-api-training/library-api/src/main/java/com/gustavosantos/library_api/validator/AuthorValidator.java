package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecord;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private AuthorRepository authorRepository;

    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void checkIfAlreadyExists(Author author) {
        if (existsAuthorRegistered(author)) {
            throw new DuplicateRecord("Author already registered!");
        }
    }

    private boolean existsAuthorRegistered(Author author) {
        Optional<Author> authorFound = authorRepository.findByFirstNameAndLastNameAndBirthDateAndNationality(
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );

        if (author.getId() == null) {
            return authorFound.isPresent();
        }

        return !author.getId().equals(authorFound.get().getId()) && authorFound.isPresent();
    }
}
