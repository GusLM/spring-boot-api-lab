package com.gustavosantos.library_api.repository.specs;

import com.gustavosantos.library_api.model.Author;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecs {

    public static Specification<Author> firstNameLike(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("firstName")), "%" + firstName.toUpperCase() + "%");
    }

    public static Specification<Author> lastNameLike(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("lastName")), "%" + lastName.toUpperCase() + "%");
    }

    public static Specification<Author> nationalityLike(String nationality) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder
                        .like(criteriaBuilder.upper(root.get("nationality"))
                                , "%" + nationality.toUpperCase() + "%");
    }
}
