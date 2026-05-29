package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByPublicId(UUID publicId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Author a WHERE a.publicId = :publicId")
    int deleteByPublicId(UUID publicId);
}
