package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.author.AuthorRequestDTO;
import com.gustavosantos.library_api.controller.dto.author.AuthorResponseDTO;
import com.gustavosantos.library_api.controller.dto.PageResponse;
import com.gustavosantos.library_api.controller.mappers.AuthorMapper;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
// http://localhost:8080/authors
public class AuthorController implements GenericController{

    private final AuthorService authorService;
    private final AuthorMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorRequestDTO dto) {
        Author author = mapper.toEntity(dto);
        authorService.save(author);
        return ResponseEntity.created(headerLocationGenerator(author.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<AuthorResponseDTO> findByPublicId(@PathVariable UUID publicId) {
        return authorService
                .findByPublicId(publicId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> delete(
            @PathVariable String publicId
    ) {
        authorService.delete(UUID.fromString(publicId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuthorResponseDTO>> search(
            @RequestParam(value = "firstName", required = false)
            String firstName,

            @RequestParam(value = "lastName", required = false)
            String lastName,

            @RequestParam(value = "nationality", required = false)
            String nationality,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            int page,

            @RequestParam(value = "size", defaultValue = "10")
            @Min(1)
            @Max(100)
            int size
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
    public ResponseEntity<Void> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid AuthorRequestDTO authorRequestDTO
    ) {
        authorService.update(publicId, authorRequestDTO);

        return ResponseEntity.noContent().build();
    }
}
