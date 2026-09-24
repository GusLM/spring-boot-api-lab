package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.client.ClientRequestDTO;
import com.gustavosantos.library_api.dto.client.ClientResponseDTO;
import com.gustavosantos.library_api.model.Client;
import com.gustavosantos.library_api.service.ClientService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@Tag(name = "OAuth clients")
public class ClientController implements GenericController{

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save an OAuth client", description = "Save an OAuth client in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "OAuth client saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "409", description = "OAuth client already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid OAuth client data")
    })
    public ResponseEntity<Void> save(@RequestBody @Valid ClientRequestDTO dto) {
        Client client = clientService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(client.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Find an OAuth client", description = "Find an OAuth client by its public identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OAuth client found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "OAuth client not found")
    })
    public ResponseEntity<ClientResponseDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(clientService.findClientByPublicId(publicId));
    }

    @PutMapping("/{publicId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an OAuth client", description = "Update an existing OAuth client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "OAuth client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "OAuth client not found"),
            @ApiResponse(responseCode = "409", description = "OAuth client cannot be updated or already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid OAuth client data")
    })
    public ResponseEntity<Void> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid ClientRequestDTO dto
    ) {
        clientService.update(publicId, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{publicId}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Revoke an OAuth client", description = "Revoke an OAuth client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "OAuth client revoked successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "OAuth client not found")
    })
    public ResponseEntity<Void> revoke(
            @PathVariable UUID publicId,
            @RequestParam (value = "reason", required = false)
            @Length(max = 500)
            String reason
    ) {
        clientService.revoke(publicId, reason);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{publicId}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivate an OAuth client", description = "Reactivate an OAuth client")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "OAuth client reactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "OAuth client not found")
    })
    public ResponseEntity<Void> reactivate(
            @PathVariable UUID publicId,
            @RequestParam (value = "reason", required = false)
            @Length(max = 500)
            String reason
    ) {
        clientService.reactivate(publicId, reason);
        return ResponseEntity.noContent().build();
    }
}
