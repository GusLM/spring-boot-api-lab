package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldSaveAuthor() {
        Author author = createAuthor();

        Author savedAuthor = authorRepository.save(author);

        assertThat(savedAuthor.getId()).isNotNull();
        assertThat(savedAuthor.getPublicId()).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("Maria");
        assertThat(savedAuthor.getLastName()).isEqualTo("Doe");
        assertThat(savedAuthor.getNationality()).isEqualTo("American");
        assertThat(savedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1998, 5, 3));
    }

    @Test
    void shouldUpdateAuthorWhenAuthorExists() {
        Author savedAuthor = authorRepository.save(
                createAuthor()
        );

        Author author = authorRepository.findByPublicId(savedAuthor.getPublicId());

        assertThat(author).isNotNull();

        author.setFirstName("Updated John");
        author.setLastName("Updated Doe");
        author.setNationality("Updated American");
        author.setBirthDate(LocalDate.of(1985, 1, 1));

        Author updatedAuthor = authorRepository.save(author);

        assertThat(updatedAuthor.getId()).isEqualTo(savedAuthor.getId());
        assertThat(updatedAuthor.getPublicId()).isEqualTo(savedAuthor.getPublicId());
        assertThat(updatedAuthor.getFirstName()).isEqualTo("Updated John");
        assertThat(updatedAuthor.getLastName()).isEqualTo("Updated Doe");
        assertThat(updatedAuthor.getNationality()).isEqualTo("Updated American");
        assertThat(updatedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 1, 1));
    }

    @Test
    void shouldFindAllAuthors() {
        authorRepository.save(createAuthor());
        authorRepository.save(new Author("John", "Smith", LocalDate.of(1990, 7, 20), "British"));

        List<Author> authors = authorRepository.findAll();

        assertThat(authors).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldCountAuthors() {
        long countBefore = authorRepository.count();

        authorRepository.save(createAuthor());

        long countAfter = authorRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void shouldDeleteAuthorByPublicIdWhenAuthorExists() {
        Author savedAuthor = authorRepository.save(
                createAuthor()
        );

        int deletedRows = authorRepository.deleteByPublicId(savedAuthor.getPublicId());

        assertThat(deletedRows).isEqualTo(1);
        assertThat(authorRepository.findByPublicId(savedAuthor.getPublicId())).isNull();
    }

    @Test
    void shouldReturnZeroWhenDeletingAuthorByPublicIdThatDoesNotExist() {
        UUID nonExistingPublicId = UUID.randomUUID();

        int deletedRows = authorRepository.deleteByPublicId(nonExistingPublicId);

        assertThat(deletedRows).isZero();
    }

    @Test
    void shouldSaveAuthorAndBookList() {
        Author author = createAuthor();

        author.addBook(
                new Book(
                        "99887766",
                        "BackEnd Ultimate Guide",
                        LocalDate.of(2024, 5, 3),
                        new BookGenre("Guide")
                )
        );

        Author savedAuthor = authorRepository.save(author);

        assertThat(author.getFirstName()).isEqualTo(savedAuthor.getFirstName());
        assertThat(savedAuthor.getBooks()).isNotEmpty();
    }

    private Author createAuthor() {
        Author author = new Author();
        author.setFirstName("Maria");
        author.setLastName("Doe");
        author.setNationality("American");
        author.setBirthDate(LocalDate.of(1998, 5, 3));
        return author;
    }
}
