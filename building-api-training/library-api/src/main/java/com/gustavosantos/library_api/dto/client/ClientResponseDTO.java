package com.gustavosantos.library_api.dto.client;

import java.util.UUID;
import java.util.List;

public record ClientResponseDTO(
        UUID publicId,
        String clientId,
        String redirectUri,
        List<String> scopes
) {
}
