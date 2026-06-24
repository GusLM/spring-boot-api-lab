package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.exceptions.ForbiddenOperationException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.model.Author;
import com.gustavosantos.library_api.repository.AuthorRepository;
import com.gustavosantos.library_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * Verifica se o autor informado já existe na base de dados.
     * @param author Objeto autor a ser validado.
     */
    public void checkIfAlreadyExists(Author author) {
        checkIfAlreadyExists(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthDate(),
                author.getNationality()
        );
    }

    /**
     * Valida a existência de um autor baseado nos seus campos únicos.
     * Lança DuplicateRecordException caso o autor já esteja cadastrado.
     * @param currentAuthorId ID do autor sendo processado (opcional em caso de novo registro).
     * @param firstName Primeiro nome do autor.
     * @param lastName Sobrenome do autor.
     * @param birthDate Data de nascimento.
     * @param nationality Nacionalidade.
     */
    public void checkIfAlreadyExists(
            Integer currentAuthorId,
            String firstName,
            String lastName,
            LocalDate birthDate,
            String nationality
    ) {
        if (existsAuthorRegistered(currentAuthorId, firstName, lastName, birthDate, nationality)) {
            throw new DuplicateRecordException("Author already registered!");
        }
    }
    
    /**
     * Verifica no repositório se já existe um autor com as mesmas características.
     * Considera duplicado se encontrar um registro com os mesmos dados e ID diferente do atual.
     */
    private boolean existsAuthorRegistered(
            Integer currentAuthorId,
            String firstName,
            String lastName,
            LocalDate birthDate,
            String nationality
    ) {
        Optional<Author> possibleDuplicate = authorRepository.findByFirstNameAndLastNameAndBirthDateAndNationality(
                firstName,
                lastName,
                birthDate,
                nationality
        );

        if (possibleDuplicate.isEmpty()) {
            return false;
        }

        if (currentAuthorId == null) {
            return true;
        }

        Author registeredAuthor = possibleDuplicate.get();

        return !registeredAuthor.getId().equals(currentAuthorId);
    }

    /**
     * Valida se um autor pode ser excluído do sistema.
     * A exclusão é impedida se o autor possuir livros vinculados.
     * @param publicId ID público do autor.
     */
    public void validateAuthorCanBeDeleted(UUID publicId) {
        Author author = authorRepository.searchAuthorByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        if (bookRepository.existsByAuthorsId(author.getId())) {
            throw new ForbiddenOperationException("Author cannot be deleted because it has books.");
        }
    }
}
