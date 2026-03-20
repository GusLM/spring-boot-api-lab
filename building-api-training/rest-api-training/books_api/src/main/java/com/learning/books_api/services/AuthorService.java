package com.learning.books_api.services;

import com.learning.books_api.entities.Author;
import com.learning.books_api.repositories.AuthorRepository;
import com.learning.books_api.services.exceptions.DatabaseException;
import com.learning.books_api.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    public Author findById(Long id) {
        Optional<Author> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + "not found"));
    }

    public Page<Author> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Author insert(Author author) {
        return repository.save(author);
    }

    public Author update(Long id, Author obj) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Author with id " + id + "not found");
        }
        Author entity = repository.getReferenceById(id);
        entity.setFirstName(obj.getFirstName());
        entity.setLastName(obj.getLastName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Author with id " + id + "not found");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }
}
