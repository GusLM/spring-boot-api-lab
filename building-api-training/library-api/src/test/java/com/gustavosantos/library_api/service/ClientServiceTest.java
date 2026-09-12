package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.client.ClientRequestDTO;
import com.gustavosantos.library_api.exceptions.ConflictException;
import com.gustavosantos.library_api.mappers.ClientMapper;
import com.gustavosantos.library_api.model.Client;
import com.gustavosantos.library_api.model.ClientAudit;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.repository.ClientAuditRepository;
import com.gustavosantos.library_api.repository.ClientRepository;
import com.gustavosantos.library_api.security.SecurityService;
import com.gustavosantos.library_api.validator.ClientValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientAuditRepository clientAuditRepository;

    @Mock
    private ClientMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private SecurityService securityService;

    @Mock
    private ClientValidator clientValidator;

    @InjectMocks
    private ClientService clientService;

    private ClientRequestDTO request;
    private Client client;
    private User actor;

    @BeforeEach
    void setUp() {
        request = new ClientRequestDTO(
                "library-client",
                "plain-secret",
                "https://example.com/callback",
                List.of("read", "write")
        );
        client = new Client();
        client.setClientId(request.clientId());
        client.setClientSecret(request.clientSecret());
        client.setRedirectUri(request.redirectUri());
        client.getScopes().addAll(request.scopes());
        actor = new User("admin", "password");
    }

    @Test
    void shouldSaveClientAndCreateAuditEventUsingAuthenticatedActor() {
        when(securityService.getLoggedUser()).thenReturn(actor);
        when(mapper.toEntity(request)).thenReturn(client);
        when(encoder.encode(request.clientSecret())).thenReturn("encoded-secret");
        when(clientRepository.save(client)).thenReturn(client);

        Client saved = clientService.save(request);

        assertThat(saved).isSameAs(client);
        assertThat(client.getClientSecret()).isEqualTo("encoded-secret");
        assertThat(client.getCreatedBy()).isEqualTo("admin");
        assertThat(client.getUpdatedBy()).isEqualTo("admin");
        verify(clientValidator).validateClientNotRegistered(client);

        ArgumentCaptor<ClientAudit> auditCaptor = ArgumentCaptor.forClass(ClientAudit.class);
        verify(clientAuditRepository).save(auditCaptor.capture());
        assertThat(auditCaptor.getValue().getPerformedBy()).isEqualTo("admin");
    }

    @Test
    void shouldRevokeClientAndRecordActorAndReason() {
        UUID publicId = UUID.randomUUID();
        client.setPublicId(publicId);
        when(securityService.getLoggedUser()).thenReturn(actor);
        when(clientRepository.findByPublicId(publicId)).thenReturn(Optional.of(client));

        clientService.revoke(publicId, "credential compromised");

        assertThat(client.isRevoked()).isTrue();
        assertThat(client.getRevokedBy()).isEqualTo("admin");

        ArgumentCaptor<ClientAudit> auditCaptor = ArgumentCaptor.forClass(ClientAudit.class);
        verify(clientAuditRepository).save(auditCaptor.capture());
        assertThat(auditCaptor.getValue().getEvent().name()).isEqualTo("REVOKED");
        assertThat(auditCaptor.getValue().getPerformedBy()).isEqualTo("admin");
        assertThat(auditCaptor.getValue().getReason()).isEqualTo("credential compromised");
    }

    @Test
    void shouldReactivateClientAndRecordEvent() {
        UUID publicId = UUID.randomUUID();
        client.setPublicId(publicId);
        client.revoke("SECURITY_JOB");
        when(securityService.getLoggedUser()).thenReturn(actor);
        when(clientRepository.findByPublicId(publicId)).thenReturn(Optional.of(client));

        clientService.reactivate(publicId, "issue resolved");

        assertThat(client.isRevoked()).isFalse();
        assertThat(client.getRevokedBy()).isNull();

        ArgumentCaptor<ClientAudit> auditCaptor = ArgumentCaptor.forClass(ClientAudit.class);
        verify(clientAuditRepository).save(auditCaptor.capture());
        assertThat(auditCaptor.getValue().getEvent().name()).isEqualTo("REACTIVATED");
        assertThat(auditCaptor.getValue().getPerformedBy()).isEqualTo("admin");
    }

    @Test
    void shouldNotUpdateRevokedClient() {
        UUID publicId = UUID.randomUUID();
        client.setPublicId(publicId);
        client.revoke("SECURITY_JOB");
        when(clientRepository.findByPublicId(publicId)).thenReturn(Optional.of(client));
        when(securityService.getLoggedUser()).thenReturn(actor);

        assertThatThrownBy(() -> clientService.update(publicId, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Revoked client cannot be updated");
    }
}
