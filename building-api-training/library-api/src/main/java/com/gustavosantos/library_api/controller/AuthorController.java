package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.controller.dto.PageResponse;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
// http://localhost:8080/authors
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody AuthorDTO authorDTO) {
        Author obj = authorDTO.toEntity();
        authorService.save(obj);

        URI uri  = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(obj.getPublicId())
                .toUri();

        return ResponseEntity.created(uri).build();
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
    public ResponseEntity<Void> delete(@PathVariable String publicId) {
        if (!authorService.existsByPublicId(UUID.fromString(publicId))) {
            return ResponseEntity.notFound().build();
        }

        authorService.delete(UUID.fromString(publicId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuthorDTO>> search(
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
        Page<AuthorDTO> authorDTOPage = authorService.search(
                firstName,
                lastName,
                nationality,
                page,
                size
        );

        return ResponseEntity.ok(PageResponse.from(authorDTOPage));
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<Void> update(
            @PathVariable String publicId,
            @RequestBody AuthorDTO authorDTO
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

        authorService.update(author);

        return ResponseEntity.noContent().build();
    }
}
