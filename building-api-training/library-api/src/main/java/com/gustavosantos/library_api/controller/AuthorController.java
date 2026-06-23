package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.controller.dto.PageResponse;
import com.gustavosantos.library_api.controller.dto.exceptions.StandardError;
import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.service.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
// http://localhost:8080/authors
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO authorDTO, HttpServletRequest request) {
        Author obj = authorDTO.toEntity();

        try {
            authorService.save(obj);

            URI uri  = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{publicId}")
                    .buildAndExpand(obj.getPublicId())
                    .toUri();

            return ResponseEntity.created(uri).build();
        } catch (DuplicateRecordException e) {
            var standardError = StandardError.conflict(e.getMessage(), request);
            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        }
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<AuthorResponseDTO> findByPublicId(@PathVariable String publicId) {
        Optional<AuthorResponseDTO> authorOptional = authorService.findByPublicId(UUID.fromString(publicId));

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AuthorResponseDTO dto = authorOptional.get();
        return ResponseEntity.ok(dto);

    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<Object> delete(
            @PathVariable String publicId,
            HttpServletRequest request
    ) {
        try {
            authorService.delete(UUID.fromString(publicId));

            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            var standardError = StandardError.resourceNotFound(e.getMessage(), request);
            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        } catch (ForbiddenOperationException e) {
            var standardError = StandardError.forbidden(e.getMessage(), request);
            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        }
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
            int page,

            @RequestParam(value = "size", defaultValue = "10")
            int size
    ) {
        Page<AuthorResponseDTO> authorResponseDTOS = authorService.searchByExample(
                firstName,
                lastName,
                nationality,
                page,
                size
        );

        return ResponseEntity.ok(PageResponse.from(authorResponseDTOS));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<Object> update(
            @PathVariable String publicId,
            @RequestBody @Valid AuthorDTO authorDTO,
            HttpServletRequest request
    ) {

        Optional<Author> authorOptional =
                authorService.searchAuthorByPublicId(UUID.fromString(publicId));

        if (authorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Author author = authorOptional.get();
        author.setFirstName(authorDTO.firstName());
        author.setLastName(authorDTO.lastName());
        author.setBirthDate(authorDTO.birthDate());
        author.setNationality(authorDTO.nationality());

        try {
            authorService.update(author);

            return ResponseEntity.noContent().build();

        } catch (DuplicateRecordException e) {
            var standardError = StandardError.conflict(e.getMessage(), request);
            return ResponseEntity.status(standardError.getStatus()).body(standardError);
        }
    }
}
