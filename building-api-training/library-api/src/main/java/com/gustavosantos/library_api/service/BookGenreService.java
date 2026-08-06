package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreSearchResultDTO;
import com.gustavosantos.library_api.controller.mappers.BookGenreMapper;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.validator.BookGenreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gustavosantos.library_api.repository.specs.BookGenreSpecs.*;

import java.util.UUID;

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

    @Transactional(readOnly = true)
    public BookGenreSearchResultDTO findByPublicId(UUID publicId) {
        BookGenre bookGenre = bookGenreRepository
                .findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + publicId));

        return mapper.toSearchResultDto(bookGenre);
    }

    @Transactional
    public void deleteByPublicId(UUID publicId) {
        if (bookGenreRepository.deleteByPublicId(publicId) == 0) {
            throw new ResourceNotFoundException("Genre not found with id: " + publicId);
        }
    }

    @Transactional(readOnly = true)
    public Page<BookGenreSearchResultDTO> search(
            String genre,
            Integer page,
            Integer pageSize
    ) {
        Specification<BookGenre> specs =
                Specification.
                        where((root, query, criteriaBuilder) ->
                                criteriaBuilder.conjunction());

        if (genre != null) {
            specs = specs.and(genreNameLike(genre));
        }

        Pageable pageable = PageRequest.of(page, pageSize);

        return bookGenreRepository.findAll(specs, pageable).map(mapper::toSearchResultDto);
    }
}
