package com.learning.books_api.services;

import com.learning.books_api.entities.Category;
import com.learning.books_api.repositories.CategoryRepository;
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
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    public Category findById(Long id) {
        Optional<Category> category = repository.findById(id);
        return category.orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public Page<Category> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Category insert(Category category) {
        return repository.save(category);
    }

    public Category update(Long id, Category obj) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        Category category = repository.getReferenceById(id);
        category.setName(obj.getName());
        return repository.save(category);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

}
