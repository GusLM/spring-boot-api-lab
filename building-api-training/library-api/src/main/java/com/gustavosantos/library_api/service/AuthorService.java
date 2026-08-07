package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.controller.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.controller.mappers.AuthorMapper;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gustavosantos.library_api.repository.specs.AuthorSpecs.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorValidator validator;
    private final AuthorMapper mapper;

    @Transactional
    public Author save(AuthorRequestDTO dto) {
        Author author = mapper.toEntity(dto);
        validator.checkIfAlreadyExists(author);
       return authorRepository.save(author);
    }

    @Transactional
    public void update(UUID publicId, AuthorRequestDTO authorRequestDTO) {
        Author author = authorRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        validator.checkIfAlreadyExists(
                author.getId(),
                authorRequestDTO.firstName(),
                authorRequestDTO.lastName(),
                authorRequestDTO.birthDate(),
                authorRequestDTO.nationality()
        );

        author.setFirstName(authorRequestDTO.firstName());
        author.setLastName(authorRequestDTO.lastName());
        author.setBirthDate(authorRequestDTO.birthDate());
        author.setNationality(authorRequestDTO.nationality());
    }

    @Transactional(readOnly = true)
    public AuthorResponseDTO findByPublicId(UUID publicId) {
        Author author = authorRepository
                .findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + publicId));

        return mapper.toResponseDto(author);
    }

    @Transactional
    public void delete(UUID publicId) {
        Author author = authorRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));
        validator.validateAuthorCanBeDeleted(author);
        authorRepository.deleteByPublicId(publicId);
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
            Integer page,
            Integer size
    ) {
        Specification<Author> specs =
                Specification.where((root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction());


        if (firstName != null) {
            specs = specs.and(firstNameLike(firstName));
        }

        if (lastName != null) {
            specs = specs.and(lastNameLike(lastName));
        }

        if (nationality != null) {
            specs = specs.and(nationalityLike(nationality));
        }

        Pageable pageable = PageRequest.of(page, size);

        return authorRepository.findAll(specs, pageable).map(mapper::toResponseDto);

//        String normalizedFirstName = normalize(firstName);
//        String normalizedLastName = normalize(lastName);
//        String normalizedNationality = normalize(nationality);
//
//        if (normalizedFirstName == null && normalizedLastName == null && normalizedNationality == null) {
//            return authorRepository.searchAll(pageable);
//        }
//
//        return authorRepository.search(
//                toLikePattern(normalizedFirstName),
//                toLikePattern(normalizedLastName),
//                toLikePattern(normalizedNationality),
//                pageable
//        );
    }

    public Author findAuthorByPublicId(UUID publicId) {
        return authorRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + publicId));
    }

//    private String normalize(String value) {
//        return hasText(value) ? value.trim() : null;
//    }
//
//    private String toLikePattern(String value) {
//        return value == null ? "%" : "%" + value + "%";
//    }
//
//    private boolean hasText(String value) {
//        return value != null && !value.isBlank();
//    }
}
