package com.gustavosantos.library_api.controller.mappers;

import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BookGenreMapper {

    BookGenreRepository bookGenreRepository;

    public BookGenreMapper(BookGenreRepository bookGenreRepository) {
        this.bookGenreRepository = bookGenreRepository;
    }

    @Mapping(target = "bookGenre", source = "genrePublicId", qualifiedByName = "mapBookGenre")
    public abstract BookGenre toEntity(BookGenreRequestDTO dto);

    @Named("mapBookGenre")
    protected BookGenre mapBookGenre(UUID genrePublicId) {
        return bookGenreRepository
                .findByPublicId(genrePublicId)
                .orElseThrow(() -> new ResourceNotFoundException("Book genre not found: " + genrePublicId));
    }
}
