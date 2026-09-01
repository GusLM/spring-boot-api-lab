package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.user.UserRequestDTO;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements GenericController{

    private final UserService userService;

    @PostMapping
    // Cadastro de usuários fica restrito a ADMIN para evitar criação livre de contas e privilégios.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid UserRequestDTO dto) {
        User user = userService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(user.getPublicId())).build();
    }
}
