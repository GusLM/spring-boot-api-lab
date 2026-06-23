package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.validator.AuthorValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;

    public AuthorService(AuthorRepository authorRepository, AuthorValidator validator) {
        this.authorRepository = authorRepository;
        this.validator = validator;
    }

    public void save(Author author) {
        validator.checkIfAlreadyExists(author);
        authorRepository.save(author);
    }

    public void update(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("The author ID does not exist in the database.");
        }

        validator.checkIfAlreadyExists(author);

        authorRepository.save(author);
    }

    public Optional<AuthorResponseDTO> findByPublicId(UUID publicId) {
        return authorRepository.findByPublicId(publicId);
    }

    public Optional<Author> searchAuthorByPublicId(UUID publicId) {
        return authorRepository.searchAuthorByPublicId(publicId);
    }

    public void delete(UUID publicId) {
        validator.validateAuthorCanBeDeleted(publicId);
        authorRepository.deleteByPublicId(publicId);
    }

    public boolean existsByPublicId(UUID publicId) {
        return authorRepository.existsByPublicId(publicId);
    }

    public Page<AuthorDTO> search(
            String firstName,
            String lastName,
            String nationality,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        boolean hasFirstName = hasText(firstName);
        boolean hasLastName = hasText(lastName);
        boolean hasNationality = hasText(nationality);

        if (hasFirstName && hasLastName && hasNationality) {
            return authorRepository.findByFirstNameAndLastNameAndNationality(
                    firstName,
                    lastName,
                    nationality,
                    pageable
            );
        }

        if (hasFirstName && hasLastName) {
            return authorRepository.findByFirstNameAndLastName(firstName, lastName, pageable);
        }

        if (hasFirstName && hasNationality) {
            return authorRepository.findByFirstNameAndNationality(
                    firstName,
                    nationality,
                    pageable
            );
        }

        if (hasLastName && hasNationality) {
            return authorRepository.findByLastNameAndNationality(
                    lastName,
                    nationality,
                    pageable
            );
        }

        if (hasFirstName) {
            return authorRepository.findByFirstName(firstName, pageable);
        }

        if (hasLastName) {
            return authorRepository.findByLastName(lastName, pageable);
        }

        if (hasNationality) {
            return authorRepository.findByNationality(nationality, pageable);
        }

        return authorRepository.findAllCustom(pageable);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
