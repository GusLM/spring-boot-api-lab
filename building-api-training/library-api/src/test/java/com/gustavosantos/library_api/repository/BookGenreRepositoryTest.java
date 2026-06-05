package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
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
        BookGenre savedBookGenre = saveBookGenre(Genre.FICTION);

        assertThat(savedBookGenre.getId()).isNotNull();
        assertThat(savedBookGenre.getPublicId()).isNotNull();
        assertThat(savedBookGenre.getGenre()).isEqualTo(Genre.FICTION);
    }

    @Test
    void shouldUpdateBookGenreWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre(Genre.FANTASY);

        savedBookGenre.setGenre(Genre.ROMANCE);
        BookGenre updatedBookGenre = bookGenreRepository.saveAndFlush(savedBookGenre);

        assertThat(updatedBookGenre.getId()).isEqualTo(savedBookGenre.getId());
        assertThat(updatedBookGenre.getPublicId()).isEqualTo(savedBookGenre.getPublicId());
        assertThat(updatedBookGenre.getGenre()).isEqualTo(Genre.ROMANCE);
    }

    @Test
    void shouldFindAllBookGenres() {
        saveBookGenre(Genre.MYSTERY);
        saveBookGenre(Genre.THRILLER);

        List<BookGenre> bookGenres = bookGenreRepository.findAll();

        assertThat(bookGenres)
                .extracting(BookGenre::getGenre)
                .contains(Genre.MYSTERY, Genre.THRILLER);
    }

    @Test
    void shouldCountBookGenres() {
        long countBefore = bookGenreRepository.count();

        saveBookGenre(Genre.HORROR);

        long countAfter = bookGenreRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void shouldFindBookGenreByPublicIdWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre(Genre.ADVENTURE);

        BookGenre foundBookGenre = bookGenreRepository.findByPublicId(savedBookGenre.getPublicId());

        assertThat(foundBookGenre).isNotNull();
        assertThat(foundBookGenre.getId()).isEqualTo(savedBookGenre.getId());
        assertThat(foundBookGenre.getGenre()).isEqualTo(Genre.ADVENTURE);
    }

    @Test
    void shouldReturnNullWhenFindingByPublicIdThatDoesNotExist() {
        BookGenre foundBookGenre = bookGenreRepository.findByPublicId(UUID.randomUUID());

        assertThat(foundBookGenre).isNull();
    }

    @Test
    void shouldFindBookGenreByGenreWhenBookGenreExists() {
        saveBookGenre(Genre.SCIENCE_FICTION);

        BookGenre foundBookGenre = bookGenreRepository.findByGenre(Genre.SCIENCE_FICTION);

        assertThat(foundBookGenre).isNotNull();
        assertThat(foundBookGenre.getGenre()).isEqualTo(Genre.SCIENCE_FICTION);
    }

    @Test
    void shouldReturnNullWhenFindingByGenreThatDoesNotExist() {
        BookGenre foundBookGenre = bookGenreRepository.findByGenre(Genre.BIOGRAPHY);

        assertThat(foundBookGenre).isNull();
    }

    @Test
    void shouldDeleteBookGenreWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre(Genre.HISTORY);

        bookGenreRepository.delete(savedBookGenre);
        bookGenreRepository.flush();

        assertThat(bookGenreRepository.findById(savedBookGenre.getId())).isEmpty();
    }

    @Test
    void shouldDeleteBookGenreByPublicIdWhenBookGenreExists() {
        BookGenre savedBookGenre = saveBookGenre(Genre.SELF_HELP);

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

    private BookGenre saveBookGenre(Genre genre) {
        return bookGenreRepository.saveAndFlush(createBookGenre(genre));
    }

    private BookGenre createBookGenre(Genre genre) {
        return new BookGenre(genre);
    }
}
