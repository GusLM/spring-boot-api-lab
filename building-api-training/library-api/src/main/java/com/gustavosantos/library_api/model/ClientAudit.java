package com.gustavosantos.library_api.model;

import com.gustavosantos.library_api.enums.ClientAuditEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "client_audit", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 30)
    private ClientAuditEvent event;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @CreatedDate
    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    @Column(length = 500)
    private String reason;

    private ClientAudit(
            Client client,
            ClientAuditEvent event,
            String performedBy,
            LocalDateTime performedAt,
            String reason
    ) {
        this.client = client;
        this.event = event;
        this.performedBy = performedBy;
        this.performedAt = performedAt;
        this.reason = reason;
    }

    public static ClientAudit revoked(
            Client client,
            String performedBy,
            String reason
    ) {
        return new ClientAudit(
                client,
                ClientAuditEvent.REVOKED,
                performedBy,
                LocalDateTime.now(),
                reason
        );
    }

    public static ClientAudit created(
            Client client,
            String performedBy
    ) {
        return new ClientAudit(
                client,
                ClientAuditEvent.CREATED,
                performedBy,
                LocalDateTime.now(),
                null
        );
    }

    public static ClientAudit updated(
            Client client,
            String performedBy,
            String reason
    ) {
        return new ClientAudit(
                client,
                ClientAuditEvent.UPDATED,
                performedBy,
                LocalDateTime.now(),
                reason
        );
    }

    public static ClientAudit reactivated(
            Client client,
            String performedBy,
            String reason
    ) {
        return new ClientAudit(
                client,
                ClientAuditEvent.REACTIVATED,
                performedBy,
                LocalDateTime.now(),
                reason
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientAudit that = (ClientAudit) o;
        return Objects.equals(id, that.id) && Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client);
    }
}
