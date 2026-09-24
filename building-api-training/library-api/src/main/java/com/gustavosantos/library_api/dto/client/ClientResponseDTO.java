package com.gustavosantos.library_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;
import java.util.List;

@Schema(name = "ClientResponse", description = "OAuth client data returned by the API")
public record ClientResponseDTO(
        UUID publicId,
        String clientId,
        String redirectUri,
        List<String> scopes
) {
}
