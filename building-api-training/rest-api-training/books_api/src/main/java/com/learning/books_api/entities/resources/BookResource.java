package com.learning.books_api.entities.resources;

import com.learning.books_api.dto.PageResponse;
import com.learning.books_api.entities.Book;
import com.learning.books_api.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/books")
public class BookResource {

    @Autowired
    private BookService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        Book obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Book>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Book> result = service.findAll(page, size);
        return ResponseEntity.ok().body(PageResponse.from(result));
    }

    @PostMapping
    public ResponseEntity<Book> insert(@RequestBody Book obj) {
        obj = service.insert(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book obj) {
        service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
