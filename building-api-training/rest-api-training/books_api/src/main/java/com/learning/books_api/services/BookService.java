package com.learning.books_api.services;

import com.learning.books_api.dto.BookDTO;
import com.learning.books_api.entities.Author;
import com.learning.books_api.entities.Book;
import com.learning.books_api.entities.Category;
import com.learning.books_api.entities.Publisher;
import com.learning.books_api.repositories.AuthorRepository;
import com.learning.books_api.repositories.BookRepository;
import com.learning.books_api.repositories.CategoryRepository;
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
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public Book findById(Long id) {
        Optional<Book> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Page<Book> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Book insert(Book book) {
        return repository.save(book);
    }

    public Book update(Long id, BookDTO dto) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        Book entity = repository.getReferenceById(id);
        updateBook(entity, dto);
        return repository.save(entity);
    }

    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateBook(Book entity, BookDTO dto) {
        entity.setTitle(dto.getTitle());
        entity.setLaunchDate(dto.getLaunchDate());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.getReferenceById(dto.getCategoryId());
            entity.setCategory(category);
        }

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.getReferenceById(dto.getPublisherId());
            entity.setPublisher(publisher);
        }

        if (dto.getAuthorIds() != null) {
            entity.getAuthors().clear();
            for (Long authorId : dto.getAuthorIds()) {
                Author author = authorRepository.getReferenceById(authorId);
                entity.addAuthor(author);
            }
        }
    }
}
