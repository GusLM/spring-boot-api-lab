package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.controller.mappers.BookGenreMapper;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.validator.BookGenreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreMapper mapper;
    private final BookGenreValidator validator;


    @Transactional
    public BookGenre save(BookGenreRequestDTO dto) {
        BookGenre bookGenre = mapper.toEntity(dto);
        validator.validateBookGenreNotRegistered(bookGenre);
        return bookGenreRepository.save(bookGenre);
    }
}
