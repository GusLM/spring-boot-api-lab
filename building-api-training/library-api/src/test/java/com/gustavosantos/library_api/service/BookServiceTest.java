package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.mappers.BookMapper;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import com.gustavosantos.library_api.validator.BookValidator;
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
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookGenreRepository bookGenreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper mapper;

    @Mock
    private BookValidator bookValidator;

    @InjectMocks
    private BookService bookService;

    private UUID genrePublicId;
    private UUID authorPublicId;
    private BookGenre genre;
    private Author author;
    private Book book;
    private BookRequestDTO request;

    @BeforeEach
    void setUp() {
        genrePublicId = UUID.randomUUID();
        authorPublicId = UUID.randomUUID();
        genre = new BookGenre("Fantasy");
        author = new Author("J. R. R.", "Tolkien", LocalDate.of(1892, 1, 3), "British");
        book = new Book("978-1234567890", "The Hobbit", LocalDate.of(1937, 9, 21), genre);
        request = new BookRequestDTO(
                "978-1234567890",
                "The Hobbit",
                LocalDate.of(1937, 9, 21),
                genrePublicId,
                List.of(authorPublicId)
        );
    }

    @Test
    void shouldSaveBookWithItsAuthors() {
        // Valida, associa os autores encontrados e persiste o livro.
        when(mapper.toEntity(request)).thenReturn(book);
        when(authorRepository.findByPublicId(authorPublicId)).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.save(request);

        assertThat(result).isSameAs(book);
        assertThat(book.getAuthors()).containsExactly(author);
        verify(bookValidator).validateIsbnNotRegistered(null, request.isbn());
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowWhenSavingBookWithUnknownAuthor() {
        // Impede o cadastro quando um autor informado não existe.
        when(mapper.toEntity(request)).thenReturn(book);
        when(authorRepository.findByPublicId(authorPublicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.save(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id: " + authorPublicId);

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldFindBookByPublicId() {
        // Busca o livro e converte a entidade para o DTO de resposta.
        UUID publicId = UUID.randomUUID();
        BookSearchResultDTO response = new BookSearchResultDTO(
                publicId, book.getIsbn(), book.getTitle(), book.getPublicationDate(), "Fantasy", List.of()
        );
        when(bookRepository.findByPublicId(publicId)).thenReturn(Optional.of(book));
        when(mapper.toSearchResultDto(book)).thenReturn(response);

        assertThat(bookService.findByPublicId(publicId)).isSameAs(response);
        verify(mapper).toSearchResultDto(book);
    }

    @Test
    void shouldThrowWhenFindingUnknownBook() {
        // Lança exceção quando o livro não é encontrado.
        UUID publicId = UUID.randomUUID();
        when(bookRepository.findByPublicId(publicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findByPublicId(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id: " + publicId);
    }

    @Test
    void shouldDeleteBookWhenItExists() {
        // Remove o livro somente depois de confirmar a sua existência.
        UUID publicId = UUID.randomUUID();
        when(bookRepository.existsByPublicId(publicId)).thenReturn(true);

        bookService.delete(publicId);

        verify(bookRepository).deleteByPublicId(publicId);
    }

    @Test
    void shouldThrowWhenDeletingUnknownBook() {
        // Impede a exclusão de um livro inexistente.
        UUID publicId = UUID.randomUUID();
        when(bookRepository.existsByPublicId(publicId)).thenReturn(false);

        assertThatThrownBy(() -> bookService.delete(publicId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id: " + publicId);

        verify(bookRepository, never()).deleteByPublicId(publicId);
    }

    @Test
    void shouldSearchBooksWithFiltersAndPagination() {
        // Aplica os filtros, pagina os resultados e converte os livros retornados.
        Pageable pageable = PageRequest.of(1, 10);
        BookSearchResultDTO response = new BookSearchResultDTO(
                UUID.randomUUID(), book.getIsbn(), book.getTitle(), book.getPublicationDate(), "Fantasy", List.of()
        );
        when(bookRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(book), pageable, 1));
        when(mapper.toSearchResultDto(book)).thenReturn(response);

        var result = bookService.search("978-1234567890", "Hobbit", 1937, "Fantasy", "Tolkien", 1, 10);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getNumber()).isEqualTo(1);
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldUpdateBookAndAuthors() {
        // Atualiza os dados do livro e substitui seus autores relacionados.
        UUID bookPublicId = UUID.randomUUID();
        UUID newGenrePublicId = UUID.randomUUID();
        UUID newAuthorPublicId = UUID.randomUUID();
        BookGenre newGenre = new BookGenre("Adventure");
        Author oldAuthor = new Author("Old", "Author", LocalDate.of(1970, 1, 1), "Canadian");
        Author newAuthor = new Author("New", "Author", LocalDate.of(1980, 1, 1), "American");
        book.addAuthor(oldAuthor);
        BookRequestDTO updatedRequest = new BookRequestDTO(
                "978-0987654321", "The Fellowship", LocalDate.of(1954, 7, 29), newGenrePublicId,
                List.of(newAuthorPublicId)
        );
        when(bookRepository.findByPublicId(bookPublicId)).thenReturn(Optional.of(book));
        when(bookGenreRepository.findByPublicId(newGenrePublicId)).thenReturn(Optional.of(newGenre));
        when(authorRepository.findByPublicId(newAuthorPublicId)).thenReturn(Optional.of(newAuthor));

        bookService.update(bookPublicId, updatedRequest);

        verify(bookValidator).validateIsbnNotRegistered(null, updatedRequest.isbn());
        assertThat(book.getIsbn()).isEqualTo(updatedRequest.isbn());
        assertThat(book.getTitle()).isEqualTo(updatedRequest.title());
        assertThat(book.getPublicationDate()).isEqualTo(updatedRequest.publicationDate());
        assertThat(book.getGenre()).isSameAs(newGenre);
        assertThat(book.getAuthors()).containsExactly(newAuthor);
        assertThat(oldAuthor.getBooks()).doesNotContain(book);
    }

    @Test
    void shouldThrowWhenUpdatingWithUnknownGenre() {
        // Impede a atualização quando o novo gênero não existe.
        UUID bookPublicId = UUID.randomUUID();
        when(bookRepository.findByPublicId(bookPublicId)).thenReturn(Optional.of(book));
        when(bookGenreRepository.findByPublicId(genrePublicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(bookPublicId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Genre not found with id: " + genrePublicId);
    }

    @Test
    void shouldThrowWhenUpdatingUnknownBook() {
        // Impede a atualização quando o livro não existe.
        UUID bookPublicId = UUID.randomUUID();
        when(bookRepository.findByPublicId(bookPublicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(bookPublicId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id: " + bookPublicId);

        verify(bookGenreRepository, never()).findByPublicId(any(UUID.class));
    }

    @Test
    void shouldThrowWhenUpdatingWithUnknownAuthor() {
        // Impede a atualização quando um dos novos autores não existe.
        UUID bookPublicId = UUID.randomUUID();
        when(bookRepository.findByPublicId(bookPublicId)).thenReturn(Optional.of(book));
        when(bookGenreRepository.findByPublicId(genrePublicId)).thenReturn(Optional.of(genre));
        when(authorRepository.findByPublicId(authorPublicId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(bookPublicId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id: " + authorPublicId);
    }
}
