package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    //@Transactional
    public void insert(Author author) {
        authorRepository.save(author);
    }

    public Optional<AuthorResponseDTO> findByPublicId(UUID publicId) {
        return authorRepository.findByPublicId(publicId);
    }

    //@Transactional
    public void delete(UUID publicId) {
        authorRepository.deleteAuthorByPublicId(publicId);
    }

    public boolean existsByPublicId(UUID publicId) {
        return authorRepository.existsAuthorByPublicId(publicId);
    }
}
