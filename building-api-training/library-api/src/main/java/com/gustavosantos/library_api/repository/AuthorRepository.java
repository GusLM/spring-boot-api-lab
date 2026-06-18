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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    // Busca um autor pelo seu ID público e retorna um AuthorResponseDTO
    Optional<AuthorResponseDTO> findByPublicId(UUID publicId);

    // Busca a entidade Author completa pelo seu ID público
    Optional<Author> searchAuthorByPublicId(UUID publicId);

    // Busca autores pelo primeiro nome utilizando paginação
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

    // Busca autores pelo sobrenome utilizando paginação
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

    // Busca autores pela nacionalidade utilizando paginação
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

    // Busca autores por nome, sobrenome e nacionalidade utilizando paginação
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

    // Busca autores por nome e sobrenome utilizando paginação
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

    // Busca autores por nome e nacionalidade utilizando paginação
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

    // Busca autores por sobrenome e nacionalidade utilizando paginação
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

    // Busca todos os autores retornando um AuthorDTO customizado com paginação
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

    // Verifica se um autor existe pelo seu ID público
    boolean existsByPublicId(UUID publicId);
}
