package com.gustavosantos.library_api.service;

import com.gustavosantos.library_api.dto.client.ClientRequestDTO;
import com.gustavosantos.library_api.dto.client.ClientResponseDTO;
import com.gustavosantos.library_api.exceptions.ConflictException;
import com.gustavosantos.library_api.exceptions.ResourceNotFoundException;
import com.gustavosantos.library_api.mappers.ClientMapper;
import com.gustavosantos.library_api.model.Client;
import com.gustavosantos.library_api.model.ClientAudit;
import com.gustavosantos.library_api.repository.ClientAuditRepository;
import com.gustavosantos.library_api.repository.ClientRepository;
import com.gustavosantos.library_api.security.SecurityService;
import com.gustavosantos.library_api.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private static final String SYSTEM_ACTOR = "SYSTEM";

    private final ClientRepository clientRepository;
    private final ClientAuditRepository clientAuditRepository;
    private final ClientMapper mapper;
    private final PasswordEncoder encoder;
    private final SecurityService securityService;
    private final ClientValidator clientValidator;

    @Transactional
    public Client save(ClientRequestDTO dto) {
        return save(dto, resolveActor());
    }

    @Transactional
    public Client save(ClientRequestDTO dto, String actor) {
        var client = mapper.toEntity(dto);
        clientValidator.validateClientNotRegistered(client);
        client.setClientSecret(encoder.encode(client.getClientSecret()));
        client.setCreatedBy(actor);
        client.setUpdatedBy(actor);

        Client savedClient = clientRepository.save(client);
        clientAuditRepository.save(ClientAudit.created(savedClient, actor));
        return savedClient;
    }

    @Transactional
    public void revoke(UUID publicId, String reason) {
        revoke(publicId, resolveActor(), reason);
    }

    @Transactional
    public void revoke(UUID publicId, String actor, String reason) {
        Client client = findByPublicId(publicId);
        client.revoke(actor);
        clientAuditRepository.save(ClientAudit.revoked(client, actor, reason));
    }

    @Transactional
    public void reactivate(UUID publicId, String reason) {
        reactivate(publicId, resolveActor(), reason);
    }

    @Transactional
    public void reactivate(UUID publicId, String actor, String reason) {
        Client client = findByPublicId(publicId);
        client.reactivate(actor);
        clientAuditRepository.save(ClientAudit.reactivated(client, actor, reason));
    }

    @Transactional
    public void update(UUID publicId, ClientRequestDTO dto) {
        update(publicId, dto, resolveActor());
    }

    @Transactional
    public void update(UUID publicId, ClientRequestDTO dto, String actor) {
        Client client = findByPublicId(publicId);

        clientValidator.validateClientNotRegistered(dto.clientId(), client.getId());

        if (client.isRevoked()) {
            throw new ConflictException("Revoked client cannot be updated");
        }

        client.setClientId(dto.clientId());
        client.setClientSecret(encoder.encode(dto.clientSecret()));
        client.setRedirectUri(dto.redirectUri());
        client.getScopes().clear();
        client.getScopes().addAll(dto.scopes());
        client.setUpdatedBy(actor);

        clientAuditRepository.save(ClientAudit.updated(client, actor, null));
    }

    @Transactional(readOnly = true)
    public ClientResponseDTO findClientByPublicId(UUID publicId) {
        Client client = clientRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + publicId));

        return mapper.toResponse(client);
    }

    private String resolveActor() {
        var loggedUser = securityService.getLoggedUser();
        return loggedUser != null ? loggedUser.getLogin() : SYSTEM_ACTOR;
    }

    private Client findByPublicId(UUID publicId) {
        return clientRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + publicId));
    }
}
