package com.gustavosantos.library_api.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;
import java.net.URI;

public interface GenericController {

    default URI headerLocationGenerator(UUID id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
