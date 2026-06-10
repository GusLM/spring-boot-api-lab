package com.gustavosantos.library_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books_genres", schema = "public")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId;

    @Setter
    @Column(name = "genre", length = 45, nullable = false)
    private String genre;

    @CreatedDate
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @LastModifiedBy
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_id")
    private int userId;

    // Constructor
    public BookGenre(String genre) {
        this.genre = genre;
    }

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private List<Book> books;

    @PrePersist
    private void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
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
