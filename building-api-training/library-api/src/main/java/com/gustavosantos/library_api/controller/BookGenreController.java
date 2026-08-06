package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.controller.dto.bookgenre.BookGenreSearchResultDTO;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.service.BookGenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/book-genres")
public class BookGenreController implements GenericController{

    private final BookGenreService bookGenreService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid BookGenreRequestDTO dto) {
        BookGenre bookGenre = bookGenreService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(bookGenre.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<BookGenreSearchResultDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(bookGenreService.findByPublicId(publicId));
    }
}
