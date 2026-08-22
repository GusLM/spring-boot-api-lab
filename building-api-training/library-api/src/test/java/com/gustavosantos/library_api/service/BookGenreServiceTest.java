package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreSearchResultDTO;
import com.gustavosantos.library_api.controller.mappers.BookGenreMapper;
import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.validator.BookGenreValidator;
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
class BookGenreServiceTest {

    @Mock
    private BookGenreRepository bookGenreRepository;

    @Mock
    private BookGenreMapper mapper;

    @Mock
    private BookGenreValidator validator;

    @InjectMocks
    private BookGenreService bookGenreService;

    private BookGenreRequestDTO request;
    private BookGenre genre;

    @BeforeEach
    void setUp() {
        request = new BookGenreRequestDTO("Fantasy");
        genre = new BookGenre(request.genre());
    }

    @Test
    void shouldSaveGenreAfterValidatingIt() {
        // Converte, valida e persiste o gênero informado.
        when(mapper.toEntity(request)).thenReturn(genre);
        when(bookGenreRepository.save(genre)).thenReturn(genre);

        assertThat(bookGenreService.save(request)).isSameAs(genre);

        verify(validator).validateBookGenreNotRegistered(genre);
        verify(bookGenreRepository).save(genre);
    }

    @Test
    void shouldFindGenreByPublicId() {
        // Busca o gênero e converte a entidade para o DTO de resposta.
        UUID publicId = UUID.randomUUID();
        BookGenreSearchResultDTO response = new BookGenreSearchResultDTO(publicId, "Fantasy");
        when(bookGenreRepository.findByPublicId(publicId)).thenReturn(Optional.of(genre));
        when(mapper.toSearchResultDto(genre)).thenReturn(response);

        assertThat(bookGenreService.findByPublicId(publicId)).isSameAs(response);
    }

    @Test
    void shouldThrowWhenFindingUnknownGenre() {
        // Lança exceção quando o gênero não é encontrado.
        UUID publicId = UUID.randomUUID();
        when(bookGenreRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookGenreService.findByPublicId(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id: " + publicId);
    }

    @Test
    void shouldDeleteGenreWhenRepositoryDeletesIt() {
        // Conclui a exclusão quando o repositório remove um registro.
        UUID publicId = UUID.randomUUID();
        when(bookGenreRepository.deleteByPublicId(publicId)).thenReturn(1);

        bookGenreService.deleteByPublicId(publicId);

        verify(bookGenreRepository).deleteByPublicId(publicId);
    }

    @Test
    void shouldThrowWhenDeletingUnknownGenre() {
        // Lança exceção quando nenhum gênero é removido.
        UUID publicId = UUID.randomUUID();
        when(bookGenreRepository.deleteByPublicId(publicId)).thenReturn(0);

        assertThatThrownBy(() -> bookGenreService.deleteByPublicId(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Genre not found with id: " + publicId);
    }

    @Test
    void shouldSearchGenresWithFilterAndPagination() {
        // Filtra os gêneros, pagina os resultados e converte as entidades.
        Pageable pageable = PageRequest.of(1, 10);
        BookGenreSearchResultDTO response = new BookGenreSearchResultDTO(UUID.randomUUID(), "Fantasy");
        when(bookGenreRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(genre), pageable, 1));
        when(mapper.toSearchResultDto(genre)).thenReturn(response);

        var result = bookGenreService.search("Fantasy", 1, 10);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getNumber()).isEqualTo(1);
        verify(bookGenreRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldUpdateGenreWhenItExists() {
        // Valida e altera o nome do gênero existente.
        UUID publicId = UUID.randomUUID();
        BookGenreRequestDTO updatedRequest = new BookGenreRequestDTO("History");
        when(bookGenreRepository.findByPublicId(publicId)).thenReturn(Optional.of(genre));
        when(bookGenreRepository.findByGenre(updatedRequest.genre())).thenReturn(Optional.empty());

        bookGenreService.update(publicId, updatedRequest);

        verify(validator).validateBookGenreNotRegistered(genre);
        assertThat(genre.getGenre()).isEqualTo("History");
    }

    @Test
    void shouldThrowWhenUpdatingUnknownGenre() {
        // Impede a atualização quando o gênero não existe.
        UUID publicId = UUID.randomUUID();
        when(bookGenreRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookGenreService.update(publicId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Genre not found with id: " + publicId);

        verify(validator, never()).validateBookGenreNotRegistered(any(BookGenre.class));
    }

    @Test
    void shouldThrowWhenUpdatingToExistingGenreName() {
        // Impede a alteração para um nome que já está cadastrado.
        UUID publicId = UUID.randomUUID();
        BookGenreRequestDTO updatedRequest = new BookGenreRequestDTO("History");
        when(bookGenreRepository.findByPublicId(publicId)).thenReturn(Optional.of(genre));
        when(bookGenreRepository.findByGenre(updatedRequest.genre())).thenReturn(Optional.of(new BookGenre("History")));

        assertThatThrownBy(() -> bookGenreService.update(publicId, updatedRequest))
                .isInstanceOf(DuplicateRecordException.class)
                .hasMessage("Genre 'History' already registered!");

        assertThat(genre.getGenre()).isEqualTo("Fantasy");
    }
}
