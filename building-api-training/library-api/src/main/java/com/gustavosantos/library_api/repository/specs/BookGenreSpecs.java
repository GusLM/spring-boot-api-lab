package com.gustavosantos.library_api.repository.specs;

import com.gustavosantos.library_api.model.BookGenre;
import org.springframework.data.jpa.domain.Specification;

public class BookGenreSpecs {

    public static Specification<BookGenre> genreNameLike(String genreName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("genre")), "%" + genreName.toUpperCase() + "%");
    }
}
