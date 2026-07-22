package com.gustavosantos.library_api.controller.mappers;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    AuthorRepository authorRepository;

    public BookMapper(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Mapping(target = "author", source = "authorsPublicIds", qualifiedByName = "mapAuthors")
    public abstract Book toEntity(BookRequestDTO dto);


    @Named("mapAuthors")
    protected List<Author> mapAuthors(List<UUID> authorsPublicIds) {
        return authorsPublicIds.stream()
                .map(id -> authorRepository.findEntityByPublicId(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id)))
                .toList();
    }
}
