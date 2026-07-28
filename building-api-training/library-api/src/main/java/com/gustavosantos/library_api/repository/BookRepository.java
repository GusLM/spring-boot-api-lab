package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

    Book findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthors(Author author);

    boolean existsByAuthorsId(Integer authorId);

    @Query("SELECT a FROM Book b JOIN b.authors a")
    List<Author> findAllAuthorsWhoHaveBooks();

    Optional<Book> findByPublicId(UUID publicId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Book b WHERE b.publicId = :publicId")
    void deleteByPublicId(UUID publicId);

    boolean existsByPublicId(UUID publicId);
}