package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.model.Book;
import com.gustavosantos.library_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BookValidator {

    private final BookRepository bookRepository;

    /**
     * Valida se o ISBN informado já está sendo usado por outro livro.
     *
     * Esse método serve tanto para cadastro quanto para atualização:
     *
     * - No cadastro, o currentBookId normalmente será null. Se já existir
     *   qualquer livro com o mesmo ISBN, será considerado duplicado.
     *
     * - Na atualização, o currentBookId representa o ID interno do livro
     *   que está sendo alterado. Nesse caso, só será considerado duplicado
     *   se o ISBN encontrado pertencer a outro livro, e não ao próprio livro
     *   em edição.
     *
     * @param currentBookId ID interno do livro atual. Pode ser null em cadastro.
     * @param isbn ISBN que será validado.
     * @throws DuplicateRecordException quando outro livro já usa o mesmo ISBN.
     */
    public void validateIsbnNotRegistered(Integer currentBookId, String isbn) {
        if (existsAnotherBookWithIsbn(currentBookId, isbn)) {
            throw new DuplicateRecordException("Book already registered!");
        }
    }

    /**
     * Verifica se existe outro livro cadastrado com o mesmo ISBN.
     *
     * A lógica funciona assim:
     *
     * 1. Busca no banco algum livro com o ISBN informado.
     * 2. Se não encontrar nenhum livro, não existe duplicidade.
     * 3. Se encontrar um livro e currentBookId for null, significa que é um
     *    cadastro novo. Portanto, qualquer livro encontrado com esse ISBN
     *    representa duplicidade.
     * 4. Se encontrar um livro e currentBookId não for null, significa que é
     *    uma atualização. Nesse caso, compara o ID do livro encontrado com o
     *    ID do livro atual:
     *
     *    - IDs iguais: é o próprio livro sendo atualizado, então não é duplicado.
     *    - IDs diferentes: é outro livro usando o mesmo ISBN, então é duplicado.
     *
     * @param currentBookId ID interno do livro atual. Pode ser null em cadastro.
     * @param isbn ISBN que será pesquisado no banco.
     * @return true se existir outro livro com o mesmo ISBN; false caso contrário.
     */
    private boolean existsAnotherBookWithIsbn(Integer currentBookId, String isbn) {
        Optional<Book> possibleDuplicate = bookRepository.findByIsbn(isbn);

        if (possibleDuplicate.isEmpty()) {
            return false;
        }

        if (currentBookId == null) {
            return true;
        }

        Book registeredBook = possibleDuplicate.get();

        return !registeredBook.getId().equals(currentBookId);
    }
}