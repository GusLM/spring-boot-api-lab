package com.gustavosantos.library_api.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "ClientRequest", description = "Data required to register or update an OAuth client")
public record ClientRequestDTO(

        @NotBlank(message = "required field")
        @Size(max = 100)
        String clientId,

        @NotBlank(message = "required field")
        @Size(max = 255)
        String clientSecret,

        @NotBlank(message = "required field")
        @Size(max = 2048)
        String redirectUri,

        @NotEmpty(message = "empty list not allowed")
        List<String> scopes
) {
}
