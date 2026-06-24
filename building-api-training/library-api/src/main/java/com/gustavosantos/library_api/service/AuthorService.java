package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;

    @Transactional
    public void save(Author author) {
        validator.checkIfAlreadyExists(author);
        authorRepository.save(author);
    }

    @Transactional
    public void update(UUID publicId, AuthorDTO authorDTO) {
        Author author = authorRepository.searchAuthorByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        validator.checkIfAlreadyExists(
                author.getId(),
                authorDTO.firstName(),
                authorDTO.lastName(),
                authorDTO.birthDate(),
                authorDTO.nationality()
        );

        author.setFirstName(authorDTO.firstName());
        author.setLastName(authorDTO.lastName());
        author.setBirthDate(authorDTO.birthDate());
        author.setNationality(authorDTO.nationality());
    }

    @Transactional(readOnly = true)
    public Optional<AuthorResponseDTO> findByPublicId(UUID publicId) {
        return authorRepository.findByPublicId(publicId);
    }

    @Transactional(readOnly = true)
    public Optional<Author> searchAuthorByPublicId(UUID publicId) {
        return authorRepository.searchAuthorByPublicId(publicId);
    }

    @Transactional
    public void delete(UUID publicId) {
        validator.validateAuthorCanBeDeleted(publicId);
        authorRepository.deleteByPublicId(publicId);
    }

    @Transactional(readOnly = true)
    public boolean existsByPublicId(UUID publicId) {
        return authorRepository.existsByPublicId(publicId);
    }

    /*
    ***** Exemplo de Query By Example *****
    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> searchByExample(
            String firstName,
            String lastName,
            String nationality,
            int page,
            int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        var author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setNationality(nationality);


        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Author> authorExample = Example.of(author, matcher);

        Page<Author> authors = authorRepository.findAll(authorExample, pageable);

        return authors.map(a -> new AuthorResponseDTO(
                a.getPublicId(),
                a.getFirstName(),
                a.getLastName(),
                a.getBirthDate(),
                a.getNationality()
        ));
    }
     */

    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> search(
            String firstName,
            String lastName,
            String nationality,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        String normalizedFirstName = normalize(firstName);
        String normalizedLastName = normalize(lastName);
        String normalizedNationality = normalize(nationality);

        if (normalizedFirstName == null && normalizedLastName == null && normalizedNationality == null) {
            return authorRepository.searchAll(pageable);
        }

        return authorRepository.search(
                toLikePattern(normalizedFirstName),
                toLikePattern(normalizedLastName),
                toLikePattern(normalizedNationality),
                pageable
        );
    }

    private String normalize(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private String toLikePattern(String value) {
        return value == null ? "%" : "%" + value + "%";
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
