package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.user.UserRequestDTO;
import com.gustavosantos.library_api.model.User;
import com.gustavosantos.library_api.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users")
public class UserController implements GenericController{

    private final UserService userService;

    @PostMapping
    // Cadastro de usuários fica restrito a ADMIN para evitar criação livre de contas e privilégios.
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save a user", description = "Save a user in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "User login already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid user data")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid UserRequestDTO dto) {
        User user = userService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(user.getPublicId())).build();
    }
}
