package com.gustavosantos.library_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books", schema = "public")
@Getter
@NoArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId;

    @Setter
    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Setter
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Setter
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Setter
    @CreatedDate
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Setter
    @LastModifiedBy
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @Column(name = "user_id")
    private Integer userId;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genre_id", nullable = false)
    private BookGenre genre;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private final List<Author> authors = new ArrayList<>();

    public Book(String isbn, String title, LocalDate publicationDate, BookGenre genre) {
        this.isbn = isbn;
        this.title = title;
        this.publicationDate = publicationDate;
        this.genre = genre;
    }

    @PrePersist
    private void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(publicId, book.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }
}
