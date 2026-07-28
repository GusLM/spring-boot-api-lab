package com.gustavosantos.library_api.repository.specs;

import com.gustavosantos.library_api.model.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> isbnEqual(String isbn) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> titleLike(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
    }

    public static Specification<Book> publicationYearEqual(Integer publicationYear) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.function("to_char", String.class,
                        root.get("publicationDate"), criteriaBuilder.literal("YYYY")), publicationYear.toString());
    }

    public static Specification<Book> genreNameLike(String genreName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("genre").get("genre")
                        ),"%" + genreName.toUpperCase() + "%");
    }

    public static Specification<Book> authorNameLike(String authorName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.upper(root.get("authors").get("firstName")
                        ),"%" + authorName.toUpperCase() + "%");
    }
}
