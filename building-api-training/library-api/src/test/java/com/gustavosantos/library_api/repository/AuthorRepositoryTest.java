package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void saveTest() {
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setNationality("American");
        author.setBirthDate(LocalDate.of(1980, 1, 1));

        var authorSaved = authorRepository.save(author);
        System.out.println(authorSaved);
    }

    @Test
    public void updateTest() {
        UUID publicId = UUID.fromString("f2fa5162-f08d-469a-89b6-1a9aa1ac44a8");
        Author author = authorRepository.findByPublicId(publicId);
        if (author == null) {
            throw new IllegalArgumentException("Author not found with UUID: " + publicId);
        }
        author.setFirstName("Updated John");
        author.setLastName("Updated Doe");
        author.setNationality("Updated American");
        author.setBirthDate(LocalDate.of(1985, 1, 1));

        var authorUpdated = authorRepository.save(author);
        System.out.println(authorUpdated);
    }


}
