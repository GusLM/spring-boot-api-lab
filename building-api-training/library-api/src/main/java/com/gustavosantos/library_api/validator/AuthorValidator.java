package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AuthorValidator {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorValidator(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void checkIfAlreadyExists(Author author) {
        if (existsAuthorRegistered(author)) {
            throw new DuplicateRecordException("Author already registered!");
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

    public void validateAuthorCanBeDeleted(UUID publicId) {
        Author author = authorRepository.searchAuthorByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        if (bookRepository.existsByAuthorsId(author.getId())) {
            throw new ForbiddenOperationException("Author cannot be deleted because it has books.");
        }
    }
}
