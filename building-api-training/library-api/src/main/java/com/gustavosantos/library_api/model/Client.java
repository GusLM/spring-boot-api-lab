package com.gustavosantos.library_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "client", schema = "public")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "client_id", nullable = false, length = 100, unique = true)
    private String clientId;

    @Column(name = "client_secret", nullable = false, length = 255)
    private String clientSecret;

    @Column(name = "redirect_uri", nullable = false, length = 2048)
    private String redirectUri;

    @CreatedDate
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "revoked_by", length = 100)
    private String revokedBy;

    @ElementCollection
    @CollectionTable(
            name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id")
    )
    @Column(name = "scope", nullable = false, length = 100)
    private final List<String> scopes = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    public void revoke(String actor) {
        if (isRevoked()) {
            throw new IllegalStateException("Client is already revoked");
        }

        revokedAt = LocalDateTime.now();
        revokedBy = actor;
        updatedBy = actor;
    }

    public void reactivate(String actor) {
        if (!isRevoked()) {
            throw new IllegalStateException("Client is already active");
        }

        revokedAt = null;
        revokedBy = null;
        updatedBy = actor;
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(publicId, client.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(publicId);
    }
}
