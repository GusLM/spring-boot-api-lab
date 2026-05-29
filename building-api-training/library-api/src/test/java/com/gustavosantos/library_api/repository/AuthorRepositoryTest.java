package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
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
        Author author = createAuthor("Maria", "Doe", "American", LocalDate.of(1985, 3, 13));

        Author savedAuthor = authorRepository.save(author);

        assertThat(savedAuthor.getId()).isNotNull();
        assertThat(savedAuthor.getPublicId()).isNotNull();
        assertThat(savedAuthor.getFirstName()).isEqualTo("Maria");
        assertThat(savedAuthor.getLastName()).isEqualTo("Doe");
        assertThat(savedAuthor.getNationality()).isEqualTo("American");
        assertThat(savedAuthor.getBirthDate()).isEqualTo(LocalDate.of(1985, 3, 13));
    }

    @Test
    void shouldUpdateAuthorWhenAuthorExists() {
        Author savedAuthor = authorRepository.save(
                createAuthor("John", "Doe", "American", LocalDate.of(1985, 1, 1))
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
        authorRepository.save(createAuthor("Maria", "Doe", "American", LocalDate.of(1985, 3, 13)));
        authorRepository.save(createAuthor("John", "Smith", "British", LocalDate.of(1990, 7, 20)));

        List<Author> authors = authorRepository.findAll();

        assertThat(authors).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldCountAuthors() {
        long countBefore = authorRepository.count();

        authorRepository.save(createAuthor("Maria", "Doe", "American", LocalDate.of(1985, 3, 13)));

        long countAfter = authorRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 1);
    }

    @Test
    void shouldDeleteAuthorByPublicIdWhenAuthorExists() {
        Author savedAuthor = authorRepository.save(
                createAuthor("Maria", "Doe", "American", LocalDate.of(1985, 3, 13))
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

    private Author createAuthor(String firstName, String lastName, String nationality, LocalDate birthDate) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setNationality(nationality);
        author.setBirthDate(birthDate);
        return author;
    }
}
