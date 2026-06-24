package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.controller.dto.AuthorResponseDTO;
import com.gustavosantos.library_api.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    // Busca um autor pelo seu ID público e retorna um AuthorResponseDTO
    Optional<AuthorResponseDTO> findByPublicId(UUID publicId);

    // Busca a entidade Author completa pelo seu ID público
    Optional<Author> searchAuthorByPublicId(UUID publicId);

    // Busca um autor pelos dados completos para verificar duplicidade
    Optional<Author> findByFirstNameAndLastNameAndBirthDateAndNationality(
            String firstName, String lastName, LocalDate birthdate, String nationality
    );

    // Verifica se existe um autor com os dados fornecidos
    boolean existsByFirstNameAndLastNameAndBirthDateAndNationality(
            String firstName, String lastName, LocalDate birthdate, String nationality
    );

    // Deleta um autor pelo seu ID público
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Author a WHERE a.publicId = :publicId")
    void deleteByPublicId(UUID publicId);

    @Query("""
        select new com.gustavosantos.library_api.controller.dto.AuthorResponseDTO(
            a.publicId,
            a.firstName,
            a.lastName,
            a.birthDate,
            a.nationality
        )
        from Author a
        order by a.firstName, a.lastName
        """)
    Page<AuthorResponseDTO> searchAll(Pageable pageable);

    @Query("""
        select new com.gustavosantos.library_api.controller.dto.AuthorResponseDTO(
            a.publicId,
            a.firstName,
            a.lastName,
            a.birthDate,
            a.nationality
        )
        from Author a
        where lower(a.firstName) like :firstName
          and lower(a.lastName) like :lastName
          and lower(a.nationality) like :nationality
        order by a.firstName, a.lastName
        """)
    Page<AuthorResponseDTO> search(
            String firstName,
            String lastName,
            String nationality,
            Pageable pageable
    );

    // Verifica se um autor existe pelo seu ID público
    boolean existsByPublicId(UUID publicId);
}
