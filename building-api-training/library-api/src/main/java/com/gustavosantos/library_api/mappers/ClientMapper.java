package com.gustavosantos.library_api.mappers;

import com.gustavosantos.library_api.dto.client.ClientRequestDTO;
import com.gustavosantos.library_api.dto.client.ClientResponseDTO;
import com.gustavosantos.library_api.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "revokedAt", ignore = true)
    @Mapping(target = "revokedBy", ignore = true)
    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toResponse(Client client);
}
