package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.PageResponse;
import com.gustavosantos.library_api.dto.bookgenre.BookGenreRequestDTO;
import com.gustavosantos.library_api.dto.bookgenre.BookGenreSearchResultDTO;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.service.BookGenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/book-genres")
@Tag(name = "Book genres")
public class BookGenreController implements GenericController{

    private final BookGenreService bookGenreService;

    @PostMapping
    // USER e ADMIN podem criar gêneros; usuários sem esses papéis recebem acesso negado.
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Save a book genre", description = "Save a book genre in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book genre saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "Book genre already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid book genre data")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid BookGenreRequestDTO dto) {
        BookGenre bookGenre = bookGenreService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(bookGenre.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    // Sem @PreAuthorize aqui, vale a regra global: precisa estar autenticado, mas não exige papel específico.
    @Operation(summary = "Find a book genre", description = "Find a book genre by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book genre found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Book genre not found")
    })
    public ResponseEntity<BookGenreSearchResultDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(bookGenreService.findByPublicId(publicId));
    }

    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete a book genre", description = "Delete a book genre by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book genre deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book genre not found")
    })
    public ResponseEntity<Void> deleteByPublicId(@PathVariable UUID publicId) {
        bookGenreService.deleteByPublicId(publicId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Search book genres", description = "Search book genres using an optional filter and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book genres returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameter"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PageResponse<BookGenreSearchResultDTO>> search(
            @RequestParam(value = "genre-name", required = false)
            String genre,
            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            @Min(1)
            @Max(100)
            Integer pageSize
    ) {
        return ResponseEntity.ok(PageResponse.from(bookGenreService.search(genre, page, pageSize)));
    }

    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update a book genre", description = "Update an existing book genre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book genre updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book genre not found"),
            @ApiResponse(responseCode = "409", description = "Book genre already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid book genre data")
    })
    public ResponseEntity<Void> update(@PathVariable UUID publicId, @RequestBody @Valid BookGenreRequestDTO dto) {
        bookGenreService.update(publicId, dto);
        return ResponseEntity.noContent().build();
    }
}
