package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.BookGenre;
import com.gustavosantos.library_api.model.enums.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

    BookGenre findByPublicId(UUID publicId);

    BookGenre findByGenre(Genre genre);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BookGenre bg WHERE bg.publicId = :publicId")
    int deleteByPublicId(UUID publicId);
}
