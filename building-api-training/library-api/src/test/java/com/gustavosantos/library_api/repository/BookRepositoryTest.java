package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

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

    private Book createBook() {
        return new Book("12345678910111213", "Java: Programming Language", LocalDate.of(2023, 1, 1), createBookGenre());
    }

    private BookGenre createBookGenre() {
        return new BookGenre(Genre.GUIDE);
    }

}
