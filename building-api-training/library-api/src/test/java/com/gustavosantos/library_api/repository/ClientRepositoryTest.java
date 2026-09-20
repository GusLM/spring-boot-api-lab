package com.gustavosantos.library_api.repository;

import com.gustavosantos.library_api.model.Client;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.flyway.enabled=false")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldLoadScopesWhenFindingClientByClientId() {
        Client client = new Client();
        client.setPublicId(UUID.randomUUID());
        client.setClientId("library-client");
        client.setClientSecret("encoded-secret");
        client.setRedirectUri("https://example.com/callback");
        client.setRegisteredAt(LocalDateTime.now());
        client.getScopes().addAll(List.of("read", "write"));
        clientRepository.saveAndFlush(client);

        Client foundClient = clientRepository.findClientByClientId("library-client");

        assertThat(foundClient).isNotNull();
        assertThat(Hibernate.isInitialized(foundClient.getScopes())).isTrue();
        assertThat(foundClient.getScopes()).containsExactlyInAnyOrder("read", "write");
    }
}
