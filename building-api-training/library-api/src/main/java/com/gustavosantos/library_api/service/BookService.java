package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.controller.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.controller.mappers.BookMapper;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import com.gustavosantos.library_api.validator.BookValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gustavosantos.library_api.repository.specs.BookSpecs.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper mapper;
    private final BookValidator bookValidator;

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

    @Transactional
    public void delete(UUID publicId) {
        if (!bookRepository.existsByPublicId(publicId)) {
            throw new ResourceNotFoundException("Book not found with id: " + publicId);
        }

        bookRepository.deleteByPublicId(publicId);
    }

    @Transactional(readOnly = true)
    public List<BookSearchResultDTO> search(
            String isbn,
            String title,
            Integer year,
            String genreName,
            String authorName
    ) {

        Specification<Book> specs =
                Specification.
                        where((root, query, criteriaBuilder) ->
                                criteriaBuilder.conjunction());

        if (isbn != null) {
            specs = specs.and(isbnEqual(isbn));
        }

        if (title != null) {
            specs = specs.and(titleLike(title));
        }

        if (year != null) {
            specs = specs.and(publicationYearEqual(year));
        }

        if (genreName != null) {
            specs = specs.and(genreNameLike(genreName));
        }

        if (authorName != null) {
            specs = specs.and(authorNameLike(authorName));
        }

        List<Book> books = bookRepository.findAll(specs);

        return books.stream().map(mapper::toSearchResultDto).toList();
    }

    public void update(UUID publicId, BookRequestDTO dto) {
        Book book = bookRepository
                .findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + publicId));

        BookGenre bookGenre = bookGenreRepository
                .findByPublicId(dto.genrePublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + dto.genrePublicId()));

        bookValidator.validateIsbnNotRegistered(book.getId(), dto.isbn());

        book.setIsbn(dto.isbn());
        book.setTitle(dto.title());
        book.setPublicationDate(dto.publicationDate());
        book.setGenre(bookGenre);
    }
}