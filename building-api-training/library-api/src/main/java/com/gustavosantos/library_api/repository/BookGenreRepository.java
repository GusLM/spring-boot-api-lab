package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Integer> {

    Optional<BookGenre> findByPublicId(UUID publicId);

    BookGenre findByGenre(String genre);

    boolean existsBookGenresByPublicId(UUID publicId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BookGenre bg WHERE bg.publicId = :publicId")
    int deleteByPublicId(UUID publicId);
}
