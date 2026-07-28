package com.gustavosantos.library_api.controller.mappers;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.controller.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses =  AuthorMapper.class)
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookGenreRepository bookGenreRepository;


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genre", source = "genrePublicId")
    public abstract Book toEntity(BookRequestDTO dto);

    @Mapping(target = "genre", source = "genre.genre")
    public abstract BookSearchResultDTO toSearchResultDto(Book book);

    protected BookGenre mapGenre(UUID genrePublicId) {
        if (genrePublicId == null) {
            return null;
        }

        return bookGenreRepository
                .findByPublicId(genrePublicId)
                .orElseThrow(() -> new ResourceNotFoundException("Book genre not found: " + genrePublicId));
    }

    @AfterMapping
    protected void mapAuthors(BookRequestDTO dto, @MappingTarget Book book) {
        List<UUID> authorsPublicIds = dto.authorsPublicIds();

        if (authorsPublicIds == null) {
            authorsPublicIds = Collections.emptyList();
        }

        authorsPublicIds.stream()
                .map(this::findAuthorByPublicId)
                .forEach(book::addAuthor);
    }

    private Author findAuthorByPublicId(UUID publicId) {
        return authorRepository.findEntityByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + publicId));
    }
}
