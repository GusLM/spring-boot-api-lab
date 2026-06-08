package com.gustavosantos.library_api.controller.dto;

import java.time.LocalDate;

public record AuthorDTO(
        String name,
        LocalDate birthDate,
        String nationality) {
}
