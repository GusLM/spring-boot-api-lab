package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.controller.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.controller.mappers.BookMapper;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController implements GenericController{

    private final BookService bookService;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookRequestDTO dto) {
        Book book = bookService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(book.getPublicId())).build();
    }

    @GetMapping("{publicId}")
    public ResponseEntity<BookSearchResultDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(bookService.findByPublicId(publicId));
    }
}
