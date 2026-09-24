package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.PageResponse;
import com.gustavosantos.library_api.dto.book.BookRequestDTO;
import com.gustavosantos.library_api.dto.book.BookSearchResultDTO;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
@Tag(name = "Books")
public class BookController implements GenericController{

    private final BookService bookService;

    @PostMapping
    // Regra de autorização em nível de método: somente usuários com papel USER ou ADMIN podem cadastrar livros.
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Save a book", description = "Save a book in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Genre or author not found"),
            @ApiResponse(responseCode = "409", description = "Book already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid book data")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid BookRequestDTO dto) {
        Book book = bookService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(book.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find a book", description = "Find a book by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookSearchResultDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(bookService.findByPublicId(publicId));
    }

    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete a book", description = "Delete a book by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID publicId) {
        bookService.delete(publicId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Search books", description = "Search books using optional filters and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameter"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PageResponse<BookSearchResultDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,

            @RequestParam(value = "title", required = false)
            String title,

            @RequestParam(value = "publication-year", required = false)
            Integer publicationYear,

            @RequestParam(value = "genre-name", required = false)
            String genreName,

            @RequestParam(value = "authorName", required = false)
            String authorName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            Integer page,

            @RequestParam(value = "page-size", defaultValue = "10")
            @Min(1)
            @Max(100)
            Integer pageSize
    ) {
        Page<BookSearchResultDTO> books = bookService
                .search(isbn, title, publicationYear, genreName, authorName, page, pageSize);

        return ResponseEntity.ok(PageResponse.from(books));
    }

    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update a book", description = "Update an existing book")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Book, genre or author not found"),
            @ApiResponse(responseCode = "409", description = "Book already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid book data")
    })
    public ResponseEntity<Void> update(@PathVariable UUID publicId, @RequestBody @Valid BookRequestDTO dto) {
        bookService.update(publicId, dto);
        return ResponseEntity.noContent().build();
    }
}
