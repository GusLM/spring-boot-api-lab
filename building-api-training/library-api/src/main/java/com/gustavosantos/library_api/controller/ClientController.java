package com.gustavosantos.library_api.controller;

import com.gustavosantos.library_api.dto.client.ClientRequestDTO;
import com.gustavosantos.library_api.dto.client.ClientResponseDTO;
import com.gustavosantos.library_api.model.Client;
import com.gustavosantos.library_api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController implements GenericController{

    private final ClientService clientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid ClientRequestDTO dto) {
        Client client = clientService.save(dto);
        return ResponseEntity.created(headerLocationGenerator(client.getPublicId())).build();
    }

    @GetMapping("/{publicId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> findByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(clientService.findClientByPublicId(publicId));
    }

    @PutMapping("/{publicId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(
            @PathVariable UUID publicId,
            @RequestBody @Valid ClientRequestDTO dto
    ) {
        clientService.update(publicId, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{publicId}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
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
