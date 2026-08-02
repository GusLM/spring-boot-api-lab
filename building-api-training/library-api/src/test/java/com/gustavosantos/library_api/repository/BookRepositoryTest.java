package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldSaveBook() {
        Book savedBook = bookRepository.saveAndFlush(createBook());

        Book book = bookRepository.findById(savedBook.getId()).orElse(null);

        assertThat(book).isNotNull();
    }

    @Test
    void shouldUpdateBookWhenBookExists() {
        Book savedBook = bookRepository.saveAndFlush(createBook());

        Book book = bookRepository.findById(savedBook.getId()).orElse(null);

        assertThat(book).isNotNull();

        book.setTitle("Updated Book");

        bookRepository.saveAndFlush(book);

        String updatedTitle = jdbcTemplate.queryForObject(
                """
                SELECT title
                FROM books
                WHERE title = ?
                """,
                String.class,
                book.getTitle()
        );

        assertThat(updatedTitle).isEqualTo("Updated Book");
    }

    @Test
    void shouldDeleteBookWhenBookExists() {
        Book savedBook = bookRepository.saveAndFlush(createBook());
        Book book = bookRepository.findById(savedBook.getId()).orElse(null);

        assertThat(book).isNotNull();

        bookRepository.delete(book);

        Book deletedBook = bookRepository.findById(book.getId()).orElse(null);

        assertThat(deletedBook).isNull();
    }

    @Test
    void shouldNotDeleteBookWhenBookDoesNotExist() {
        Book book = bookRepository.saveAndFlush(createBook());

        bookRepository.delete(book);

        Book deletedBook = bookRepository.findById(book.getId()).orElse(null);

        assertThat(deletedBook).isNull();
    }

    @Test
    void shouldCountBooks() {
        Book savedBook = bookRepository.saveAndFlush(createBook());
        Book book = bookRepository.findById(savedBook.getId()).orElse(null);

        assertThat(book).isNotNull();

        long count = bookRepository.count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldFindByIsbn() {
        Book book = bookRepository.saveAndFlush(createBook());
        Optional<Book> foundBook = bookRepository.findByIsbn(book.getIsbn());

        assertThat(foundBook.isPresent());

        Book foundedBook = new Book();
        if (foundBook.isPresent()) {
            foundedBook = foundBook.get();
        }

        assertThat(foundedBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void shouldFindByTitleIgnoreCase() {
        Book book = bookRepository.saveAndFlush(createBook());
        List<Book> foundBooks = bookRepository.findByTitleContainingIgnoreCase("java");

        assertThat(foundBooks).isNotNull();
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.getFirst().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void shouldFindByAuthors() {
        Book book = createBook();
        Author author = new Author("John", "Smith", LocalDate.of(1990, 7, 20), "British");
        authorRepository.saveAndFlush(author);
        book.addAuthor(author);
        bookRepository.saveAndFlush(book);
        List<Book> foundBooks = bookRepository.findByAuthors(author);

        assertThat(foundBooks).isNotNull();
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.getFirst().getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void shouldFindAuthorsWhoHaveBooks() {
        Author author = new Author("John", "Smith", LocalDate.of(1990, 7, 20), "British");
        Author author2 = new Author("Joseph", "Joestar", LocalDate.of(2000, 6, 12), "American");
        authorRepository.saveAndFlush(author);
        authorRepository.saveAndFlush(author2);

        Book book = createBook();
        book.addAuthor(author);
        book.addAuthor(author2);

        bookRepository.saveAndFlush(book);

        List<Author> authors = bookRepository.findAllAuthorsWhoHaveBooks();

        assertThat(authors).isNotNull();
        assertThat(authors).hasSizeGreaterThan(1);
    }

    private Book createBook() {
        return new Book("12345-67890", "Java: Programming Language", LocalDate.of(2023, 1, 1), createBookGenre());
    }

    private BookGenre createBookGenre() {
        BookGenre bookGenre = new BookGenre("Guide");
        return bookGenreRepository.save(bookGenre);
    }
}
