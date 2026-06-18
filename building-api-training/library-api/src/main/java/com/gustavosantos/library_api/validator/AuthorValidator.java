package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecord;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    private final AuthorRepository authorRepository;

    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void checkIfAlreadyExists(Author author) {
        if (existsAuthorRegistered(author)) {
            throw new DuplicateRecord("Author already registered!");
        }
    }
    
    private boolean existsAuthorRegistered(Author author) {
        Optional<Author> possibleDuplicate = authorRepository.findByFirstNameAndLastNameAndBirthDateAndNationality(
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );

        if (possibleDuplicate.isEmpty()) {
            return false;
        }

        if (author.getId() == null) {
            return true;
        }

        Author registeredAuthor = possibleDuplicate.get();

        return !registeredAuthor.getId().equals(author.getId());
    }
}
