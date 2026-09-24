package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.dto.PageResponse;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
@Tag(name = "Authors")
// http://localhost:8080/authors
public class AuthorController implements GenericController{

    private final AuthorService authorService;

    @PostMapping
    // @PreAuthorize é avaliado antes do método executar; sem o papel exigido, o controller nem chama o service.
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Save an author", description = "Save an author in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "422", description = "Invalid author data"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Author already exists")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRequestDTO dto) {
        Author author = authorService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(author.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find an author", description = "Find an author by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<AuthorResponseDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(authorService.findByPublicId(publicId));
    }

    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete an author", description = "Delete an author that has no associated books")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Author has associated books")
    })
    public ResponseEntity<Void> delete(
            @PathVariable String publicId
    ) {
        authorService.delete(UUID.fromString(publicId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Search authors", description = "Search authors using optional filters and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameter"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<PageResponse<AuthorResponseDTO>> search(
            @RequestParam(value = "firstName", required = false)
            String firstName,
            @RequestParam(value = "lastName", required = false)
            String lastName,
            @RequestParam(value = "nationality", required = false)
            String nationality,
            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            @Min(1)
            @Max(100)
            Integer size
    ) {
        Page<AuthorResponseDTO> authorResponseDTOS = authorService.search(
                firstName,
                lastName,
                nationality,
                page,
                size
        );

        return ResponseEntity.ok(PageResponse.from(authorResponseDTOS));
    }

    @PutMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update an author", description = "Update an existing author")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Author already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid author data")
    })
    public ResponseEntity<Void> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid AuthorRequestDTO authorRequestDTO
    ) {
        authorService.update(publicId, authorRequestDTO);

        return ResponseEntity.noContent().build();
    }
}
