package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Book save(BookRequestDTO bookRequestDTO) {
        Book book = toEntity(bookRequestDTO);
        List<Author> authorList = findAuthorsByPublicIdsOrThrow(bookRequestDTO.authorsPublicIds());

        authorList.forEach(book::addAuthor);

        return bookRepository.save(book);
    }

    private Book toEntity(BookRequestDTO bookRequestDTO) {
        BookGenre bookGenre = bookGenreRepository.findByPublicId(bookRequestDTO.genrePublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Genre not found."));

        Book book = new Book();
        book.setIsbn(bookRequestDTO.isbn());
        book.setTitle(bookRequestDTO.title());
        book.setPublicationDate(bookRequestDTO.publicationDate());
        book.setGenre(bookGenre);

        return book;
    }

    private List<Author> findAuthorsByPublicIdsOrThrow(List<UUID> authorsPublicIds) {
        return authorsPublicIds.stream()
                .map(authorPublicId -> authorRepository.findEntityByPublicId(authorPublicId)
                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorPublicId)))
                .toList();
    }
}
