package com.gustavosantos.ecommerce_api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CustomerListDTO {

    private UUID publicId;
    private String firstName;
    private String lastName;
    private String email;
}
