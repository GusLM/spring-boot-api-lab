package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.controller.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.controller.mappers.BookMapper;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper mapper;

    @Transactional
    public Book save(BookRequestDTO bookRequestDTO) {
        Book book = mapper.toEntity(bookRequestDTO);
        List<Author> authorList = findAuthorsByPublicIdsOrThrow(bookRequestDTO.authorsPublicIds());
        authorList.forEach(book::addAuthor);
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public BookSearchResultDTO findByPublicId(UUID publicId) {
        Book book = bookRepository
                .findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + publicId));

        return mapper.toSearchResultDto(book);
    }

    private List<Author> findAuthorsByPublicIdsOrThrow(List<UUID> authorsPublicIds) {
        return authorsPublicIds.stream()
                .map(authorPublicId -> authorRepository.findEntityByPublicId(authorPublicId)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorPublicId)))
                .toList();
    }
}