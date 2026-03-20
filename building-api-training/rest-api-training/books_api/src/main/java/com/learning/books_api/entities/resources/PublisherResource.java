package com.learning.books_api.entities.resources;

import com.learning.books_api.dto.PageResponse;
import com.learning.books_api.entities.Publisher;
import com.learning.books_api.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/publishers")
public class PublisherResource {

    @Autowired
    private PublisherService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Publisher> findById(@PathVariable Long id) {
        Publisher obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping
    public ResponseEntity<PageResponse<Publisher>> findALl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Publisher> result = service.findAll(page ,size);
        return ResponseEntity.ok().body(PageResponse.from(result));
    }

    @PostMapping
    public ResponseEntity<Publisher> insert(@RequestBody Publisher obj) {
        obj = service.insert(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Publisher> update(@PathVariable Long id, Publisher obj) {
        service.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
