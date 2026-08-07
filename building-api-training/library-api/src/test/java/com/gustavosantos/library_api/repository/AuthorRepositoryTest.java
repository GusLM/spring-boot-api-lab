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
import java.util.Optional;
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

        Optional<Author> authorOptional = authorRepository.findById(savedAuthor.getId());

        assertThat(authorOptional).isPresent();

        Author author = authorOptional.get();

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

        authorRepository.deleteByPublicId(savedAuthor.getPublicId());

        assertThat(authorRepository.findByPublicId(savedAuthor.getPublicId())).isEmpty();
    }

    @Test
    void shouldNotFailWhenDeletingAuthorByPublicIdThatDoesNotExist() {
        UUID nonExistingPublicId = UUID.randomUUID();

        authorRepository.deleteByPublicId(nonExistingPublicId);
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
