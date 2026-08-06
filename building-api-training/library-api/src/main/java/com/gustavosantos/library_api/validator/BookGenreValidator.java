package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.repository.BookGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookGenreValidator {

    private final BookGenreRepository bookGenreRepository;


    public void validateBookGenreNotRegistered(BookGenre bookGenre) {
        if (existsAnotherBookGenre(bookGenre)) {
            throw new DuplicateRecordException("Genre already registered!");
        }
    }

    private boolean existsAnotherBookGenre(BookGenre bookGenre) {
        Optional<BookGenre> possibleDuplicate = bookGenreRepository.findByGenre(bookGenre.getGenre());

        if (possibleDuplicate.isEmpty()) {
            return false;
        }

        if (bookGenre.getId() == null) {
            return true;
        }

        BookGenre registeredBookGenre = possibleDuplicate.get();

        return !registeredBookGenre.getId().equals(bookGenre.getId());
    }
}
