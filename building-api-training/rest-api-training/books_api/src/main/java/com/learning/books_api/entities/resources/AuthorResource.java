package com.learning.books_api.entities.resources;

import com.learning.books_api.dto.PageResponse;
import com.learning.books_api.entities.Author;
import com.learning.books_api.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/authors")
public class AuthorResource {
    
    @Autowired
    private AuthorService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Author> findById(@PathVariable Long id) {
        Author obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Author>> findALl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Author> result = service.findAll(page ,size);
        return ResponseEntity.ok().body(PageResponse.from(result));
    }

    @PostMapping
    public ResponseEntity<Author> insert(@RequestBody Author obj) {
        obj = service.insert(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Author> update(@PathVariable Long id, Author obj) {
        service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
