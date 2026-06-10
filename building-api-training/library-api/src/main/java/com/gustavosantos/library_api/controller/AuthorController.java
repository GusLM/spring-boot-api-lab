package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.service.AuthorService;
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
    public ResponseEntity<Void> insert(@RequestBody AuthorDTO authorDTO) {
        Author obj = authorDTO.toEntity();
        authorService.insert(obj);

        URI uri  = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(obj.getPublicId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<AuthorResponseDTO> findByPublicId(@PathVariable String publicId) {
        Optional<AuthorResponseDTO> authorOptional = authorService.findByPublicId(UUID.fromString(publicId));

        if (authorOptional.isPresent()) {
            AuthorResponseDTO dto = authorOptional.get();
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }
}
