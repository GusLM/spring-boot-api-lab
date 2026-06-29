package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class BookGenreRepositoryTest {

    @Autowired
    private BookGenreRepository bookGenreRepository;

    @Test
    void shouldSaveBookGenre() {
        BookGenre savedBookGenre = saveBookGenre("Fiction");

        assertThat(savedBookGenre.getId()).isNotNull();
        assertThat(savedBookGenre.getPublicId()).isNotNull();
        assertThat(savedBookGenre.getGenre()).isEqualTo("Fiction");
    }

    @Test
    void shouldUpdateBookGenreWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre("Fantasy");

        savedBookGenre.setGenre("Romance");
        BookGenre updatedBookGenre = bookGenreRepository.saveAndFlush(savedBookGenre);

        assertThat(updatedBookGenre.getId()).isEqualTo(savedBookGenre.getId());
        assertThat(updatedBookGenre.getPublicId()).isEqualTo(savedBookGenre.getPublicId());
        assertThat(updatedBookGenre.getGenre()).isEqualTo("Romance");
    }

    @Test
    void shouldFindAllBookGenres() {
        saveBookGenre("Mystery");
        saveBookGenre("Thriller");

        List<BookGenre> bookGenres = bookGenreRepository.findAll();

        assertThat(bookGenres)
                .extracting(BookGenre::getGenre)
                .contains("Mystery", "Thriller");
    }

    @Test
    void shouldCountBookGenres() {
        long countBefore = bookGenreRepository.count();

        saveBookGenre("Horror");

        long countAfter = bookGenreRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void shouldFindBookGenreByPublicIdWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre("Adventure");

        Optional<BookGenre> foundBookGenre =
                bookGenreRepository.findByPublicId(savedBookGenre.getPublicId());

        assertThat(foundBookGenre).isNotNull();
        if (foundBookGenre.isPresent()) {
            BookGenre bookGenre = foundBookGenre.get();
            assertThat(bookGenre.getId()).isEqualTo(savedBookGenre.getId());
            assertThat(bookGenre.getGenre()).isEqualTo("Adventure");
        }
    }

    @Test
    void shouldReturnNullWhenFindingByPublicIdThatDoesNotExist() {
        Optional<BookGenre> foundBookGenre = bookGenreRepository.findByPublicId(UUID.randomUUID());

        assertThat(foundBookGenre.isPresent()).isFalse();
    }

    @Test
    void shouldFindBookGenreByGenreWhenBookGenreExists() {
        saveBookGenre("Science Fiction");

        BookGenre foundBookGenre = bookGenreRepository.findByGenre("Science Fiction");

        assertThat(foundBookGenre).isNotNull();
        assertThat(foundBookGenre.getGenre()).isEqualTo("Science Fiction");
    }

    @Test
    void shouldReturnNullWhenFindingByGenreThatDoesNotExist() {
        BookGenre foundBookGenre = bookGenreRepository.findByGenre("Biography");

        assertThat(foundBookGenre).isNull();
    }

    @Test
    void shouldDeleteBookGenreWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre("History");

        bookGenreRepository.delete(savedBookGenre);
        bookGenreRepository.flush();

        assertThat(bookGenreRepository.findById(savedBookGenre.getId()).isEmpty());
    }

    @Test
    void shouldDeleteBookGenreByPublicIdWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre("Self-Help");

        int deletedRows = bookGenreRepository.deleteByPublicId(savedBookGenre.getPublicId());

        assertThat(deletedRows).isEqualTo(1);
        assertThat(bookGenreRepository.findByPublicId(savedBookGenre.getPublicId())).isNull();
    }

    @Test
    void shouldReturnZeroWhenDeletingBookGenreByPublicIdThatDoesNotExist() {
        int deletedRows = bookGenreRepository.deleteByPublicId(UUID.randomUUID());

        assertThat(deletedRows).isZero();
    }

    @Test
    void shouldNotSaveBookGenreWithoutGenre() {
        BookGenre bookGenre = new BookGenre();

        assertThatThrownBy(() -> bookGenreRepository.saveAndFlush(bookGenre))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private BookGenre saveBookGenre(String genre) {
        return bookGenreRepository.saveAndFlush(createBookGenre(genre));
    }

    private BookGenre createBookGenre(String genre) {
        return new BookGenre(genre);
    }
}
