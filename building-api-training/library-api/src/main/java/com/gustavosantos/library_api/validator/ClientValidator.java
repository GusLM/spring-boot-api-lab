package com.gustavosantos.library_api.validator;

import com.gustavosantos.library_api.exceptions.DuplicateRecordException;
import com.gustavosantos.library_api.model.Client;
import com.gustavosantos.library_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientValidator {

    private final ClientRepository clientRepository;

    public void validateClientNotRegistered(Client client) {
        validateClientNotRegistered(client.getClientId(), client.getId());
    }

    public void validateClientNotRegistered(String clientId, Long currentId) {

        if (existAnotherClientId(currentId, clientId)) {
            throw new DuplicateRecordException("Client already registered!");
        }
    }

    private boolean existAnotherClientId(Long currentId, String clientId) {
        Optional<Client> possibleDuplicate = clientRepository.findByClientId(clientId);

        if (possibleDuplicate.isEmpty()) {
            return false;
        }

        if (currentId == null) {
            return true;
        }

        Client clientRegistered = possibleDuplicate.get();

        return !clientRegistered.getId().equals(currentId);
    }
}
