package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.controller.dto.AuthorDTO;
import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<AuthorResponseDTO> findByPublicId(UUID publicId);

    Optional<Author> searchAuthorByPublicId(UUID publicId);

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.firstName LIKE :firstName
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByFirstName(String firstName, Pageable pageable);

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.lastName LIKE :lastName
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByLastName(String lastName, Pageable pageable);

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.nationality LIKE :nationality
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByNationality(String nationality, Pageable pageable);

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.nationality = :nationality
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByFirstNameAndLastNameAndNationality(
            String firstName,
            String lastName,
            String nationality,
            Pageable pageable
    );

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.firstName = :firstName AND a.lastName = :lastName
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByFirstNameAndLastName(
            String firstName,
            String lastName,
            Pageable pageable
    );

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.firstName = :firstName AND a.nationality = :nationality
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByFirstNameAndNationality(
            String firstName,
            String nationality,
            Pageable pageable
    );

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        WHERE a.lastName = :firstName AND a.nationality = :nationality
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findByLastNameAndNationality(
            String lastName,
            String nationality,
            Pageable pageable
    );

    @Query("""
                        SELECT new com.gustavosantos.library_api.controller.dto.AuthorDTO(
                        a.firstName,
                        a.lastName,
                        a.birthDate,
                        a.nationality
                        )
                        FROM Author a
                        ORDER BY a.firstName
            """)
    Page<AuthorDTO> findAllCustom(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Author a WHERE a.publicId = :publicId")
    void deleteByPublicId(UUID publicId);

    boolean existsByPublicId(UUID publicId);
}
