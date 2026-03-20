package com.learning.books_api.config;

import com.learning.books_api.entities.Author;
import com.learning.books_api.entities.Book;
import com.learning.books_api.entities.Category;
import com.learning.books_api.entities.Publisher;
import com.learning.books_api.repositories.AuthorRepository;
import com.learning.books_api.repositories.BookRepository;
import com.learning.books_api.repositories.CategoryRepository;
import com.learning.books_api.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public void run(String... args) throws Exception {

        Category category1 = new Category(null, "Fiction");
        Category category2 = new Category(null, "Non-Fiction");
        Category category3 = new Category(null, "Science Fiction");
        Category category4 = new Category(null, "Fantasy");
        Category category5 = new Category(null, "Mystery");

        categoryRepository.saveAll(Arrays.asList(category1, category2, category3, category4, category5));

        Publisher publisher1 = new Publisher(null, "Apex Publishing Group");
        Publisher publisher2 = new Publisher(null, "Core Editorial Solutions");
        Publisher publisher3 = new Publisher(null, "PrimeLeaf Publishing");
        Publisher publisher4 = new Publisher(null, "AVertex Editora");
        Publisher publisher5 = new Publisher(null, "Insight Press");

        publisherRepository.saveAll(Arrays.asList(publisher1, publisher2, publisher3, publisher4, publisher5));

        Author author1 = new Author(null, "Ryan", "Howard");
        Author author2 = new Author(null, "Michael", "Scott");
        Author author3 = new Author(null, "Dwight", "Schrute");
        Author author4 = new Author(null, "Jim", "Halpert");
        Author author5 = new Author(null, "Pamela", "Beesly");

        authorRepository.saveAll(Arrays.asList(author1, author2, author3, author4, author5));

        Book book1 = new Book(
                null,
                "The day at office",
                category1,
                publisher1,
                Instant.now()
        );

        book1.addAuthor(author1);

        Book book2 = new Book(
                null,
                "The life of Pam and Jim",
                category2,
                publisher2,
                Instant.now()
        );

        book2.addAuthor(author4);
        book2.addAuthor(author5);

        Book book3 = new Book(
                null,
                "World best boss",
                category4,
                publisher3,
                Instant.now()
        );

        book3.addAuthor(author2);

        Book book4 = new Book(
                null,
                "The next boss",
                category5,
                publisher4,
                Instant.now()
        );

        book4.addAuthor(author3);


        Book book5 = new Book(
                null,
                "The intern",
                category3,
                publisher5,
                Instant.now()
        );

        book5.addAuthor(author1);

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5));
    }
}
