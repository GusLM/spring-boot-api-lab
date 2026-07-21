package com.gustavosantos.library_api.controller.mappers;

import com.gustavosantos.library_api.controller.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.controller.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRequestDTO dto);

    AuthorRequestDTO toDto(Author author);

    AuthorResponseDTO toResponseDto(Author author);
}
