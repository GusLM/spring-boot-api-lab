package com.learning.books_api.services;

import com.learning.books_api.dto.PublisherDTO;
import com.learning.books_api.entities.Publisher;
import com.learning.books_api.repositories.PublisherRepository;
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
public class PublisherService {

    @Autowired
    private PublisherRepository repository;

    public Publisher findById(Long id) {
        Optional<Publisher> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException("Publisher with id " + id + "not found"));
    }

    public Page<Publisher> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Publisher insert(PublisherDTO dto) {
        Publisher obj = new Publisher();
        obj.setName(dto.getName());
        return repository.save(obj);
    }

    public Publisher update(Long id, PublisherDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Publisher with id " + id + "not found");
        }
        Publisher entity = repository.getReferenceById(id);
        entity.setName(dto.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Publisher with id " + id + "not found");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
