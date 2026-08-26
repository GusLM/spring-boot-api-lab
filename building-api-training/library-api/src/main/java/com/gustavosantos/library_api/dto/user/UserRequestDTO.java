package com.gustavosantos.library_api.dto.user;

import com.gustavosantos.library_api.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "required field")
        @Size(min = 5, max = 30, message = "It must be almost 5 characters and have a maximum of 30 characters")
        String login,

        @NotBlank(message = "required field")
        @Size(min = 6, max = 12, message = "Password must be almost 6 characters and have a maximum of 12 characters")
        String password,

        @NotNull(message = "required field")
        UserRole role
) {
}
