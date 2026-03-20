package com.learning.books_api.services;

import com.learning.books_api.entities.Publisher;
import com.learning.books_api.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new RuntimeException("Publisher with id " + id + "not found");
        }
    }

    public Page<Publisher> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Publisher insert(Publisher publisher) {
        return repository.save(publisher);
    }

    public Publisher update(Long id, Publisher obj) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Publisher with id " + id + "not found");
        }
        Publisher entity = repository.getReferenceById(id);
        entity.setName(obj.getName());
        return repository.save(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Publisher with id " + id + "not found");
        }
        repository.deleteById(id);
    }
}
