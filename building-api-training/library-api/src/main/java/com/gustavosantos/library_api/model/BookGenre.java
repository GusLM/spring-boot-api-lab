package com.gustavosantos.library_api.model;

import com.gustavosantos.library_api.model.enums.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books_genres", schema = "public")
@Getter
@NoArgsConstructor
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    @Setter
    private Genre genre;

    public BookGenre(Genre genre) {
        this.genre = genre;
    }

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private List<Book> books;

    @PrePersist
    private void prePersist() {
        this.publicId = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookGenre bookGenre = (BookGenre) o;
        return Objects.equals(id, bookGenre.id) && Objects.equals(publicId, bookGenre.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }
}
