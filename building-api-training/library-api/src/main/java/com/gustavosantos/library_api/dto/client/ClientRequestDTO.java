package com.gustavosantos.library_api.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

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
