package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.model.enums.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Test
    void shouldSaveBook() {
        Author author = createAuthor ("Maria", "Doe", LocalDate.of(1985, 3, 13), "American");

        Author savedAuthor = authorRepository.save(author);

        assertThat(savedAuthor).isNotNull();

        BookGenre bookGenre = bookGenreRepository.save(new BookGenre(Genre.GUIDE));

        Book book = createBook("12345678910111213", "Java: Programming Language", LocalDate.of(2023, 1, 1),  bookGenre);

        book.addAuthor(savedAuthor);

        bookRepository.save(book);
    }

    private Author createAuthor(String firstName, String lastName, LocalDate birthDate, String nationality) {
        return new Author(firstName, lastName, birthDate, nationality);
    }

    private Book createBook(String isbn, String title, LocalDate publicationDate, BookGenre genre) {
        return new Book(isbn, title, publicationDate, genre);
    }

}
