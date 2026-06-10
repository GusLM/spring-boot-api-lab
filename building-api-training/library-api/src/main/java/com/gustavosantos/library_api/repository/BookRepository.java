package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthors(Author author);

    @Query("SELECT a FROM Book b JOIN b.authors a")
    List<Author> findAllAuthorsWhoHaveBooks();
}