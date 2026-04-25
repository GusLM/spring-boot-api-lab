package com.gustavosantos.ecommerce_api.dto.customers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDTO {

    private String firstName;
    private String lastName;
    private String taxId;
    private String email;
    private String phone;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}