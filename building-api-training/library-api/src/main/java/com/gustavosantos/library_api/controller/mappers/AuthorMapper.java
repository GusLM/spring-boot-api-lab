package com.gustavosantos.library_api.controller.mappers;

import com.gustavosantos.library_api.controller.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.controller.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Author toEntity(AuthorRequestDTO dto);

    AuthorResponseDTO toResponseDto(Author author);
}
