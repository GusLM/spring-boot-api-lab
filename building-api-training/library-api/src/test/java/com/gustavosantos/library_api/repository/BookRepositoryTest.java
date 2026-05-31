package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
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
        Author author = createAuthor();

        Book book = createBook();

        book.addAuthor(author);

        Book savedBook = bookRepository.saveAndFlush(book);

        Integer relationshipCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM books_authors
                WHERE book_id = ?
                AND author_id = ?
                """,
                Integer.class,
                savedBook.getId(),
                author.getId()
        );

        assertThat(relationshipCount).isEqualTo(1);

    }

    private Author createAuthor() {
        return new Author("Maria", "Doe", LocalDate.of(1985, 3, 13), "American");
    }

    private Book createBook() {
        return new Book("12345678910111213", "Java: Programming Language", LocalDate.of(2023, 1, 1), createBookGenre());
    }

    private BookGenre createBookGenre() {
        return new BookGenre(Genre.GUIDE);
    }

}
