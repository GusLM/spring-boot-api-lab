package com.gustavosantos.library_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authors", schema = "public")
@Getter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId;

    @Setter
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Setter
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Setter
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthDate;

    @Setter
    @Column(nullable = false, length = 100)
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private final List<Book> books = new ArrayList<>();

    public Author(String firstName, String lastName, LocalDate birthDate, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    @PrePersist
    private void prePersist() {
        this.publicId = UUID.randomUUID();
    }

    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(publicId, author.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }
}
