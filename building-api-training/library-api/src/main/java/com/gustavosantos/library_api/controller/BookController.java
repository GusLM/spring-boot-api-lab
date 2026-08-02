package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.controller.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController implements GenericController{

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookRequestDTO dto) {
        Book book = bookService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(book.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<BookSearchResultDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(bookService.findByPublicId(publicId));
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        bookService.delete(publicId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BookSearchResultDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,

            @RequestParam(value = "title", required = false)
            String title,

            @RequestParam(value = "publication-year", required = false)
            Integer publicationYear,

            @RequestParam(value = "genre-name", required = false)
            String genreName,

            @RequestParam(value = "authorName", required = false)
            String authorName
    ) {
        List<BookSearchResultDTO> books = bookService.search(isbn, title, publicationYear, genreName, authorName);

        return ResponseEntity.ok(books);
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<Void> update(@PathVariable UUID publicId, @RequestBody @Valid BookRequestDTO dto) {
        bookService.update(publicId, dto);
        return ResponseEntity.noContent().build();
    }
}
