package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.book.BookCreateRequestDTO;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid BookCreateRequestDTO bookCreateRequestDTO) {
        Book book = bookService.save(bookCreateRequestDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(book.getPublicId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

}
