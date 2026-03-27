package com.learning.books_api.services;

import com.learning.books_api.dto.AuthorDTO;
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

    public Author insert(AuthorDTO dto) {
        Author obj = new Author();
        updateAuthor(obj, dto);
        return repository.save(obj);
    }

    public Author update(Long id, AuthorDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Author with id " + id + "not found");
        }
        Author entity = repository.getReferenceById(id);
        updateAuthor(entity, dto);
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

    private void updateAuthor(Author entity, AuthorDTO dto) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
    }
}
