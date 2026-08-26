package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.mappers.AuthorMapper;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.validator.AuthorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorValidator validator;

    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorService authorService;

    private AuthorRequestDTO request;
    private Author author;

    @BeforeEach
    void setUp() {
        request = new AuthorRequestDTO(
                "Maria",
                "Doe",
                LocalDate.of(1998, 5, 3),
                "American"
        );
        author = new Author(
                request.firstName(),
                request.lastName(),
                request.birthDate(),
                request.nationality()
        );
    }

    @Test
    void shouldSaveAuthorAfterValidatingIt() {
        // Converte, valida e persiste o autor informado.
        when(mapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.save(request);

        assertThat(result).isSameAs(author);
        verify(mapper).toEntity(request);
        verify(validator).checkIfAlreadyExists(author);
        verify(authorRepository).save(author);
    }

    @Test
    void shouldThrowWhenUpdatingAuthorThatDoesNotExist() {
        // Impede a atualização quando o autor não é encontrado.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.update(publicId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found.");

        verify(validator, never()).checkIfAlreadyExists(any(Integer.class), any(), any(), any(), any());
    }

    @Test
    void shouldUpdateExistingAuthor() {
        // Valida e atualiza todos os dados do autor existente.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.of(author));

        AuthorRequestDTO updatedRequest = new AuthorRequestDTO(
                "John",
                "Smith",
                LocalDate.of(1985, 1, 1),
                "British"
        );

        authorService.update(publicId, updatedRequest);

        verify(validator).checkIfAlreadyExists(
                null,
                updatedRequest.firstName(),
                updatedRequest.lastName(),
                updatedRequest.birthDate(),
                updatedRequest.nationality()
        );
        assertThat(author.getFirstName()).isEqualTo("John");
        assertThat(author.getLastName()).isEqualTo("Smith");
        assertThat(author.getBirthDate()).isEqualTo(LocalDate.of(1985, 1, 1));
        assertThat(author.getNationality()).isEqualTo("British");
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void shouldFindAuthorByPublicId() {
        // Busca o autor e converte a entidade para o DTO de resposta.
        UUID publicId = UUID.randomUUID();
        AuthorResponseDTO response = new AuthorResponseDTO(
                publicId,
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.of(author));
        when(mapper.toResponseDto(author)).thenReturn(response);

        AuthorResponseDTO result = authorService.findByPublicId(publicId);

        assertThat(result).isSameAs(response);
        verify(mapper).toResponseDto(author);
    }

    @Test
    void shouldThrowWhenFindingAuthorThatDoesNotExist() {
        // Lança exceção quando não existe autor com o ID informado.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findByPublicId(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id: " + publicId);

        verify(mapper, never()).toResponseDto(any(Author.class));
    }

    @Test
    void shouldDeleteAuthorAfterValidatingIt() {
        // Valida e remove um autor sem livros vinculados.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.of(author));

        authorService.delete(publicId);

        verify(validator).validateAuthorCanBeDeleted(author);
        verify(authorRepository).deleteByPublicId(publicId);
    }

    @Test
    void shouldNotDeleteAuthorWhenItHasBooks() {
        // Não remove o autor quando a validação identifica livros vinculados.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.of(author));
        org.mockito.Mockito.doThrow(new ForbiddenOperationException("Author cannot be deleted because it has books."))
                .when(validator).validateAuthorCanBeDeleted(author);

        assertThatThrownBy(() -> authorService.delete(publicId))
                .isInstanceOf(ForbiddenOperationException.class)
                .hasMessage("Author cannot be deleted because it has books.");

        verify(authorRepository, never()).deleteByPublicId(publicId);
    }

    @Test
    void shouldThrowWhenDeletingAuthorThatDoesNotExist() {
        // Impede a exclusão quando o autor não é encontrado.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.delete(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found.");

        verify(validator, never()).validateAuthorCanBeDeleted(any(Author.class));
        verify(authorRepository, never()).deleteByPublicId(publicId);
    }

    @Test
    void shouldSearchAuthorsWithFiltersAndPagination() {
        // Retorna autores filtrados na página solicitada.
        AuthorResponseDTO response = new AuthorResponseDTO(
                UUID.randomUUID(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );
        Pageable pageable = PageRequest.of(1, 10);
        when(authorRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(author), pageable, 1));
        when(mapper.toResponseDto(author)).thenReturn(response);

        var result = authorService.search("Maria", "Doe", "American", 1, 10);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
        verify(authorRepository).findAll(any(Specification.class), eq(pageable));
        verify(mapper).toResponseDto(author);
    }

    @Test
    void shouldSearchAuthorsWithoutOptionalFilters() {
        // Retorna uma página vazia quando não há autores cadastrados.
        Pageable pageable = PageRequest.of(0, 5);
        when(authorRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        var result = authorService.search(null, null, null, 0, 5);

        assertThat(result).isEmpty();
        verify(authorRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldFindAuthorEntityByPublicId() {
        // Retorna a entidade encontrada pelo ID público.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.of(author));

        assertThat(authorService.findAuthorByPublicId(publicId)).isSameAs(author);
    }

    @Test
    void shouldThrowWhenFindingAuthorEntityThatDoesNotExist() {
        // Lança exceção quando a entidade não é encontrada.
        UUID publicId = UUID.randomUUID();
        when(authorRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findAuthorByPublicId(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found: " + publicId);
    }
}
